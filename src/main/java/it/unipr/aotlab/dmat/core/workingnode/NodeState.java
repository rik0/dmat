package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPiece;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssign;
import it.unipr.aotlab.dmat.core.generated.OrderMultiplyWire.OrderMultiply;
import it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody;
import it.unipr.aotlab.dmat.core.loaders.MatrixMarket;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Message.MessageKind;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixValues;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMultiply;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Operation;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;
import it.unipr.aotlab.dmat.core.semirings.SemiRings;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class NodeState {
    int currentOrderSerialNo = 1;
    boolean executingOrder = false;

    WorkingNode hostWorkingNode;
    ArrayList<InNodeChunk<?>> managedChunks = new ArrayList<InNodeChunk<?>>();
    int nofAwaitingUpdate = 0;
    ArrayList<MatrixPiece> awaitingUpdate = new ArrayList<MatrixPiece>();
    LinkedList<MessageMatrixValues> chunkForUpdating
        = new LinkedList<MessageMatrixValues>();

    LinkedList<MessageMatrixValues> chunkForOperations
        = new LinkedList<MessageMatrixValues>();
    LinkedList<Operation> pendingOperations = new LinkedList<Operation>();
    LinkedList<Operation> delayedOperations = new LinkedList<Operation>();

    public boolean doesManage(String matrixId, String chunkId) {
        for (InNodeChunk<?> c : managedChunks) {
            if (c.chunk.getMatrixId().equals(matrixId)
                    && c.chunk.getChunkId().equals(chunkId))
                return true;
        }

        return false;
    }

    NodeState(WorkingNode hostWorkingNode) {
        this.hostWorkingNode = hostWorkingNode;
    }

    public boolean busyExecutingOrder() {
        return executingOrder;
    }

    public void eventuallyExecOperation() throws IOException {
        try {
            while (true) {
                Operation op = pendingOperations.removeFirst();
                op.exec(this);
            }
        } catch (NoSuchElementException e) {}

        pendingOperations.addAll(delayedOperations);
        delayedOperations.clear();
    }

    public InNodeChunk<?> getChunk(String matrixId, Rectangle interestedArea) {
        for (InNodeChunk<?> n : managedChunks) {
            if (n.chunk.getMatrixId().equals(matrixId)
                    && interestedArea.isSubset(n.chunk.getArea())) {
                return n;
            }
        }

        return null;
    }

    public InNodeChunk<?> getChunk(String matrixId, int row, int col) {
        for (InNodeChunk<?> n : managedChunks) {
            if (n.chunk.getMatrixId().equals(matrixId)
                    && n.chunk.doesManage(row, col)) {
                return n;
            }
        }

        return null;
    }

    public InNodeChunk<?> getChunk(String matrixId, String chunkId) {
        for (InNodeChunk<?> n : managedChunks) {
            if (n.chunk.getMatrixId().equals(matrixId)
                    && n.chunk.getChunkId().equals(chunkId)) {
                return n;
            }
        }

        return null;
    }


    public MessageMatrixValues getMessage(
            LinkedList<MessageMatrixValues> messages,
            String matrixId,
            Rectangle interestedArea) {
        if (messages == null) messages = chunkForOperations;

        for (MessageMatrixValues m : messages) {
            System.err.println("XXX trying "+ m.getMatrixId()+ " " + m.getPosition());
            if (m.getMatrixId().equals(matrixId)
                    && interestedArea.isSubset(m.getPosition())) {
                return m;
            }
        }

        return null;
    }

    public Iterator<Triplet> getIterator(
            LinkedList<MessageMatrixValues> extraPieces,
            String matrixId,
            Rectangle interestedArea) {

        System.err.println("XXX "+matrixId + " " + interestedArea);
        InNodeChunk<?> n = getChunk(matrixId, interestedArea);
        if (n != null) {
            return n.accessor.matrixPieceIterator(interestedArea);
        }

        MessageMatrixValues m = getMessage(extraPieces,
                                           matrixId,
                                           interestedArea);
        if (m != null) {
            return m.matrixPieceIterator();
        }

        throw new DMatInternalError("Asked for an unmanaged and unreceived piece of matrix!");
    }

    public void exec(MessageMultiply messageMultiply) throws IOException {
        LinkedList<MessageMatrixValues> missingPieces = null;

        if ((missingPieces = weGotAllPieces(messageMultiply)) != null) {
            for (OrderMultiply order : messageMultiply.body().getOperationList()) {
                System.err.println("Starting "
                        + order.getOutputMatrixId()
                        + " = " + order.getFirstFactorMatrixId()
                        + " * " + order.getSecondFactorMatrixId()
                        + " for:\n" + order.getOutputPosition());

                doTheMultiplication(missingPieces, order);
            }

            orderDone();
        }
    }

    private void doTheMultiplication(
            LinkedList<MessageMatrixValues> missingPieces,
            OrderMultiply order) throws IOException {
        String outputMatrixId = order.getOutputMatrixId();
        Rectangle outputMatrixPosition = Rectangle
                .build(order.getOutputPosition());

        InNodeChunk<?> outputPiece = getChunk(outputMatrixId,
                                              outputMatrixPosition);
        if (outputPiece != null) {
            Assertion.isTrue(outputMatrixPosition.isSubset(outputPiece
                    .chunk.getArea()),
                    "Something wrong with multiplication.");
            doTheMultImpl(missingPieces, order, MultOnLocalChunk
                    .ref(order, outputPiece));
        }
        else {
            doTheMultImpl(missingPieces, order, MulForRemoteNode
                    .ref(order,
                         getAppropriateBuilder(order.getFirstFactorMatrixId()),
                         hostWorkingNode.messageSender));
        }
    }

    private static interface MultiplicationHandles {
        void preOperation();
        void exec(Triplet t);
        void postOperation() throws IOException;
    }

    private static class MultOnLocalChunk implements MultiplicationHandles {
        InNodeChunk<?> outputPiece;
        Rectangle outputPos;

        public static MultOnLocalChunk ref(OrderMultiply order,
                                           InNodeChunk<?> outputPiece) {
            MultOnLocalChunk rv = new MultOnLocalChunk();
            rv.outputPiece = outputPiece;
            rv.outputPos = Rectangle.build(order.getOutputPosition());
            return rv;
        }

        @Override
        public void exec(Triplet t) {
            outputPiece.accessor.set(t.value(), t.row(), t.col());
        }

        @Override
        public void postOperation() {}

        @Override
        public void preOperation() {
            outputPiece.accessor.setPositionToZero(outputPos);
        }
    }

    private static class MulForRemoteNode implements MultiplicationHandles {
        private OrderMultiply order;
        private ArrayList<Triplet> results = new ArrayList<Triplet>();
        private MatrixPieces.Builder mpBuilder;
        private MessageSender messageSender;

        public static MultiplicationHandles ref(OrderMultiply order,
                                                MatrixPieces.Builder mpBuilder,
                                                MessageSender messageSender) {
            MulForRemoteNode rv = new MulForRemoteNode();
            rv.mpBuilder = mpBuilder;
            rv.order = order;
            rv.messageSender = messageSender;
            return rv;
        }

        @Override
        public void exec(Triplet t) {
            results.add(t);
        }

        @Override
        public void postOperation() throws IOException {
            it.unipr.aotlab.dmat.core.matrixPiece
                .MatrixPiece matrixPiece = mpBuilder
                        .buildFromTriplets(order.getOutputMatrixId(),
                    order.getOutputNodeId(),
                    results,
                    Rectangle.build(order.getOutputPosition()),
                    true);
            messageSender.sendMessage(mpBuilder.buildMessage(matrixPiece),
                                      order.getOutputNodeId());
        }

        @Override
        public void preOperation() {}
    }

    private void doTheMultImpl(LinkedList<MessageMatrixValues> missingPieces,
                               OrderMultiply order,
                               MultiplicationHandles eor) throws IOException {
        int endRow = order.getOutputPosition().getEndRow();
        int endCol = order.getOutputPosition().getEndCol();
        Triplet element = null;

        eor.preOperation();

        for (int row = order.getOutputPosition().getStartRow();
                row < endRow;
                ++row) {
            for (int col = order.getOutputPosition().getStartCol();
                    col < endCol;
                    ++col) {
                if ((element = doTheMulOneElement(row, col, order)) != null) {
                    eor.exec(element);
                }
            }
        }

        eor.postOperation();
    }

    private Triplet doTheMulOneElement(int row, int col, OrderMultiply order) {
        RowIterator rowIterator
            = new RowIterator(order.getFirstFactorMatrixId(), row);
        ColIterator colIterator
            = new ColIterator(order.getSecondFactorMatrixId(), col);
        SemiRing sm = SemiRings.semiring(order.getType());
        Triplet result = null;

        try {
            Triplet r = rowIterator.next();
            Triplet c = colIterator.next();

            result = r != null ? r.getCopy() : c.getCopy();
            result.setValue(sm.zero());
            result.setRow(row);
            result.setCol(col);

            // semirings' zero annihilates with respect to multiplication
            // so we skip the 0*a or a*0 operations.
            for (;;) {
                if (r.col() < c.row()) {
                    r = rowIterator.next();
                } else if (c.row() < r.col()) {
                    c = colIterator.next();
                } else { // r.row() == c.col()
                    // WARNING: in general the product is not commutative
                    Object product = sm.times(r.value(), c.value());
                    Object comulativeSum = sm.add(result.value(), product);
                    result.setValue(comulativeSum);

                    r = rowIterator.next();
                    c = colIterator.next();
                }
            }
        } catch (NoSuchElementException e) {}

        return result;
    }

    public void exec(MessageAddAssign messageAddAssign) throws IOException {
        LinkedList<MessageMatrixValues> missingPieces = null;

        if ((missingPieces = weGotAllPieces(messageAddAssign)) != null) {
            for (OrderAddAssign order : messageAddAssign.body().getOperationList())
                doTheSum(missingPieces, order);

            checkUpdatingState();
        }
    }

    private LinkedList<MessageMatrixValues> weGotAllPieces(Operation op) {
        System.err.println("XXX what op says: " + op.nofMissingPieces());

        LinkedList<MessageMatrixValues> missingPieces = getMissingPieces(op);
        if (missingPieces.size() == op.nofMissingPieces()) {
            System.err.println("Ready to do operation");
            return missingPieces;
        }

        delayedOperations.addLast(op);
        System.err.println("Still missing pieces for operation " + op.toString());

        return null;
    }

    private void doTheSum(LinkedList<MessageMatrixValues> missingPieces, OrderAddAssign order) throws IOException {
        String firstMatrixId = order.getFirstAddendumMatrixId();
        String secondMatrixId = order.getSecondAddendumMatrixId();
        SemiRing semiring = SemiRings.semiring(order.getType());
        Rectangle interestedPosition = Rectangle.build(order.getOutputPiece());

        InNodeChunk<?> firstOp = getChunk(firstMatrixId, interestedPosition);
        System.err.println(hostWorkingNode.nodeId + " doing sum for " + interestedPosition);
        if (firstOp != null) {
            //we have the output matrix piece.
            Iterator<Triplet> secondOp = getIterator(missingPieces, secondMatrixId, interestedPosition);
            while (secondOp.hasNext()) {
                Triplet t = secondOp.next();
                int r = t.row();
                int c = t.col();
                Object v = firstOp.accessor.get(r, c);
                firstOp.accessor.set(semiring.add(v, t.value()), r, c);
            }
        }
        else {
            //we do not have the output matrix piece.
            TreeSet<Triplet> tree = new TreeSet<Triplet>(new Triplet.Comparator());

            MessageMatrixValues firstOpMess = getMessage(missingPieces, firstMatrixId, interestedPosition);

            Iterator<Triplet> firstOpIt = firstOpMess.matrixPieceIterator();
            Iterator<Triplet> secondOpIt = getIterator(missingPieces, secondMatrixId, interestedPosition);

            while (firstOpIt.hasNext()) {
                Triplet fo = firstOpIt.next();
                updateSumTree(tree, fo, semiring);
            }

            while (secondOpIt.hasNext()) {
                Triplet so = secondOpIt.next();
                updateSumTree(tree, so, semiring);
            }

            sendOutputBack(tree, firstOpMess, order.getFirstAddendumNodeId());
        }
    }

    private void sendOutputBack(TreeSet<Triplet> tree,
            MessageMatrixValues firstOpMess,
            String outputNodeId) throws IOException {
        MatrixPieces.Builder b = firstOpMess.getAppropriatedBuilder();

        String matrixId = firstOpMess.getMatrixId();
        String nodeId = firstOpMess.getNodeId();
        Rectangle position = firstOpMess.getPosition();

        it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece
            rawMessage = b.buildFromTriplets(matrixId, nodeId, tree, position, true);

        hostWorkingNode.messageSender
            .sendMessage(b.buildMessage(rawMessage)
                         .recipients(outputNodeId)
                         .serialNo(currentOrderSerialNo),
                         outputNodeId);
    }

    private static void updateSumTree(TreeSet<Triplet> tree, Triplet op, SemiRing semiring) {
        Triplet otherOp = null;

        try {
            otherOp = tree.tailSet(op).first();
        } catch (java.util.NoSuchElementException t) {}

        // in semirings a + 0 = 0 + a = a
        if (otherOp != null && tree.comparator().compare(op, otherOp) == 0) {
            op.setValue(semiring.add(op.value(), otherOp.value()));
            tree.remove(otherOp);
        }

        tree.add(op);
    }

    private LinkedList<MessageMatrixValues> getMissingPieces(Operation message) {
        LinkedList<MessageMatrixValues> foundPieces = new LinkedList<MessageMatrixValues>();

        for (MessageMatrixValues chunk : chunkForOperations) {
            for (int c = message.nofMissingPieces(); c-- > 0;) {
                MatrixPieceListWire.MatrixPiece piece = message.missingPiece(c);
                Rectangle neededPos = Rectangle.build(piece.getPosition());

                if (piece.getMatrixId().equals(chunk.getMatrixId())
                        && neededPos.isSubset(chunk.getPosition())) {
                    foundPieces.add(chunk);
                    break;
                }
            }
        }

        return foundPieces;
    }

    void checkUpdatingState() {
        System.err.println("XXX awaiting update n: " + nofAwaitingUpdate);
        Iterator<MessageMatrixValues> ivalues = chunkForUpdating.iterator();
        while (ivalues.hasNext()) {
            MessageMatrixValues value = ivalues.next();

            Iterator<MatrixPiece> iwaiting = awaitingUpdate.iterator();
            while (iwaiting.hasNext()) {
                MatrixPiece waiting = iwaiting.next();

                if (value.getMatrixId().equals(waiting.getMatrixId())
                        && value.getPosition().compare(
                                waiting.getPosition()) == 0) {

                    doTheUpdate(value);

                    ivalues.remove();
                    iwaiting.remove();
                    break;
                }
            }
        }

        if (nofAwaitingUpdate == 0) orderDone();
    }

    private void doTheUpdate(MessageMatrixValues values) {
        String matrixId = values.getMatrixId();
        Rectangle position = values.getPosition();

        InNodeChunk<?> updatingNode = getChunk(matrixId, position);

        Assertion.isTrue(updatingNode != null, "Requested updating of a non-managed node!");
        Assertion.isTrue(values.getPosition().isSubset(updatingNode.chunk.getArea()),
                "Invalid updating message has arrived!");

        updatingNode.accept(values);

        --nofAwaitingUpdate;
    }

    public class RowIterator implements java.util.Iterator<Triplet> {
        String matrixId;
        int row;
        int nextChunkColumn = 0;

        Iterator<Triplet> nextTriplets = null;
        Triplet next = null;

        public RowIterator(String matrixId, int row) {
            this.matrixId = matrixId;
            this.row = row;
        }

        private void tryIterator() {
            if (nextTriplets != null && nextTriplets.hasNext()) {
                next = nextTriplets.next();
            }
            else {
                nextTriplets = null;
                next = null;
            }
        }

        private void searchManagedChunks() {
            if (nextTriplets == null) {
                InNodeChunk<?> inc = getChunk(matrixId, row, nextChunkColumn);
                if (inc != null) {
                    nextChunkColumn = inc.chunk.getEndCol();
                    nextTriplets = inc.accessor.matrixRowIterator(row);
                }
            }
        }

        private void searchMessages() {
            if (nextTriplets == null)
                for (MessageMatrixValues v : chunkForOperations) {
                    if (v.getMatrixId().equals(matrixId)
                            && v.doesManage(row, nextChunkColumn)) {

                        nextChunkColumn = v.getPosition().endCol;
                        nextTriplets = v.matrixRowterator(row);
                        break;
                    }
                }
        }

        private boolean getNextIterator() {
            searchManagedChunks();
            searchMessages();

            return nextTriplets != null;
        }

        @Override
        public boolean hasNext() {
            while (next == null && getNextIterator()) {
                tryIterator();
            }
            return next != null;
        }

        @Override
        public Triplet next() {
            if (hasNext()) {
                Triplet rvNext = next;
                next = null;

                return rvNext;
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    public class ColIterator implements java.util.Iterator<Triplet> {
        String matrixId;
        int col;
        int nextChunkRow = 0;

        Iterator<Triplet> nextTriplets = null;
        Triplet next = null;

        public ColIterator(String matrixId, int col) {
            this.matrixId = matrixId;
            this.col = col;
        }

        private void tryIterator() {
            if (nextTriplets != null && nextTriplets.hasNext()) {
                next = nextTriplets.next();
            }
            else {
                nextTriplets = null;
                next = null;
            }
        }

        private void searchManagedChunks() {
            if (nextTriplets == null)
                for (InNodeChunk<?> inc : managedChunks) {
                    if (inc.chunk.getMatrixId().equals(matrixId)
                            && inc.chunk.doesManage(nextChunkRow, col)) {

                        nextChunkRow = inc.chunk.getEndRow();
                        nextTriplets = inc.accessor.matrixColumnIterator(col);
                        break;
                    }
                }
        }

        private void searchMessages() {
            if (nextTriplets == null)
                for (MessageMatrixValues v : chunkForOperations) {
                    if (v.getMatrixId().equals(matrixId)
                            && v.doesManage(nextChunkRow, col)) {

                        nextChunkRow = v.getPosition().endRow;
                        nextTriplets = v.matrixColumnIterator(col);
                        break;
                    }
                }
        }

        private boolean getNextIterator() {
            searchManagedChunks();
            searchMessages();

            return nextTriplets != null;
        }

        @Override
        public boolean hasNext() {
            while (next == null && getNextIterator()) {
                tryIterator();
            }

            return next != null;
        }

        @Override
        public Triplet next() {
            if (hasNext()) {
                Triplet rvNext = next;
                next = null;

                return rvNext;
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public MatrixPieces.Builder getAppropriateBuilder(String matrixId) {
        for (InNodeChunk<?> node : managedChunks) {
            if (node.chunk.getMatrixId().equals(matrixId)) {
                return node.matrixPieceBuilder;
            }
        }

        for (MessageMatrixValues mess : chunkForOperations) {
            if (mess.getMatrixId().equals(matrixId)) {
                return mess.getAppropriatedBuilder();
            }
        }

        throw new DMatInternalError(hostWorkingNode.nodeId
                + " knows nothing of matrix "
                + matrixId + "! I cannot create MatrixPiece.Builder");
    }

    public void updateMatrix(MessageSetMatrix message) {
        URI dataAddress;
        InNodeChunk<?> chunk = getChunk(message.body().getMatrixId(),
                                        message.body().getChunkId());
        if (chunk == null) {
            throw new DMatInternalError("This node does not manage "
                    + message.body().getMatrixId()
                    + "." + message.body().getChunkId() + "!");
        }
        try {
            dataAddress = new URI(message.body().getURI());
        } catch (URISyntaxException e) {
            throw new DMatInternalError("Received an URI with a syntax error!");
        }
        RectangleBody rect = message.body().getPosition();
        Rectangle position = rect.getEndRow() != 0
                ? Rectangle.build(rect) : chunk.chunk.getArea();

        try {
            updateMatrixImpl(chunk, position, dataAddress, message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DMatInternalError("The file does not exist!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new DMatInternalError(e + " " + e.getMessage());
        } catch (DMatError e) {
            e.printStackTrace();
            throw new DMatInternalError(e + " " + e.getMessage());
        }
    }

    private void updateMatrixImpl(InNodeChunk<?> chunk,
                                  Rectangle position,
                                  URI dataAddress,
                                  MessageSetMatrix message)
                                          throws IOException,
                                                 DMatError {
        FileInputStream a = new FileInputStream(dataAddress.getPath());
        try {
            MatrixMarket mm = new MatrixMarket(a);
            Iterator<Triplet> mmi = mm.getIterator();

            if (mm.getNofRows() != chunk.chunk.getMatrixNofRows()
                    || mm.getNofCols() != chunk.chunk.getMatrixNofColumns()) {
                throw new DMatInternalError(
                        "Matrix on the file has wrong dimensions!");
            }

            while (mmi.hasNext()) {
                Triplet t = mmi.next();

                if (position.contains(t.row(), t.col()))
                    chunk.accessor.set(t);
            }
        } finally {
            a.close();
        }
    }

    void accept(NodeMessageDigester digester,
                Message message) throws IOException {
        boolean forUs = false;

        for (String recipient : message.recipients()) {
            if (WorkingNode.sameNode(recipient, hostWorkingNode.nodeId)) {
                forUs = true;
                break;
            }
        }

        if (forUs) {
            if (message.messageKind() == MessageKind.ORDER)
                executingOrder = true;

            message.accept(digester);
        }
        else {
            Assertion.isTrue(message.messageKind() == MessageKind.ORDER,
                    "A non-order reached this node even if it is not its recipient!\n"
                    + "Only orders should be sent to the whole NodeWorkGroup!");

            System.err.println("XXX dropped: " + message.contentType());
            ++currentOrderSerialNo;
        }
    }

    void orderDone() {
        Assertion.isTrue(executingOrder, "done an order while not doing an order?!?");

        executingOrder = false;
        ++currentOrderSerialNo;
    }
}
