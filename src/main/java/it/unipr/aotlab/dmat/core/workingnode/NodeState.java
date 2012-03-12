package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssign;
import it.unipr.aotlab.dmat.core.generated.OrderMultiplyWire.OrderMultiply;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAwaitUpdate;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixValues;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMultiply;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Operation;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;
import it.unipr.aotlab.dmat.core.semirings.SemiRings;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class NodeState {
    WorkingNode hostWorkingNode;
    ArrayList<InNodeChunk<?>> managedChunks = new ArrayList<InNodeChunk<?>>();
    ArrayList<MessageAwaitUpdate> awaitingUpdate = new ArrayList<MessageAwaitUpdate>();

    ArrayList<MessageMatrixValues> chunkForOperations = new ArrayList<MessageMatrixValues>();
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

    public MessageMatrixValues getMessage(ArrayList<MessageMatrixValues> messages,
            String matrixId,
            Rectangle interestedArea) {
        if (messages == null) messages = chunkForOperations;

        for (MessageMatrixValues m : messages) {
            if (m.getMatrixId().equals(matrixId) && interestedArea.isSubset(m.getArea()));
                return m;
        }

        return null;
    }

    public Iterator<Triplet> getIterator(ArrayList<MessageMatrixValues> extraPieces,
                                   String matrixId,
                                   Rectangle interestedArea) {
        InNodeChunk<?> n = getChunk(matrixId, interestedArea);
        if (n != null) return n.accessor.matrixPieceIterator(interestedArea);


        MessageMatrixValues m = getMessage(extraPieces, matrixId, interestedArea);
        if (m != null) return m.matrixPieceIterator();

        throw new DMatInternalError("Asked for an unmanaged and unreceived piece of matrix!");
    }

    public void exec(MessageMultiply messageMultiply) throws IOException {
        ArrayList<MessageMatrixValues> missingPieces = null;

        if ((missingPieces = weGotAllPieces(messageMultiply)) != null)
            for (OrderMultiply order : messageMultiply.body.getOperationList())
                doTheMultiplication(missingPieces, order);
    }

    private void doTheMultiplication(ArrayList<MessageMatrixValues> missingPieces,
                                     OrderMultiply order) {
        Assertion.isTrue(false, "unmplemented");
        //XXX find a way to get a column and a row iterator
    }

    public void exec(MessageAddAssign messageAddAssign) throws IOException {
        ArrayList<MessageMatrixValues> missingPieces = null;

        if ((missingPieces = weGotAllPieces(messageAddAssign)) != null)
            for (OrderAddAssign order : messageAddAssign.body.getOperationList())
                doTheSum(missingPieces, order);
    }

    private ArrayList<MessageMatrixValues> weGotAllPieces(Operation op) {
        ArrayList<MessageMatrixValues> missingPieces = getMissingPieces(op);
        if (missingPieces.size() == op.nofMissingPieces()) {
            System.err.println("Ready to do operation");
            return missingPieces;
        }

        delayedOperations.addLast(op);
        System.err.println("Still missing pieces for operation " + op.toString());
        return null;
    }

    private void doTheSum(ArrayList<MessageMatrixValues> missingPieces, OrderAddAssign order) throws IOException {
        String firstMatrixId = order.getFirstAddendumMatrixId();
        String secondMatrixId = order.getSecondAddendumMatrixId();
        SemiRing semiring = SemiRings.semiring(order.getType().getSemiRing());
        Rectangle interestedPosition = Rectangle.build(order.getOutputPiece());

        InNodeChunk<?> firstOp = getChunk(firstMatrixId, interestedPosition);
        System.err.println(interestedPosition);
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

            sendOutputBack(tree, firstOpMess);
        }
    }

    private void sendOutputBack(TreeSet<Triplet> tree, MessageMatrixValues firstOpMess) throws IOException {
        MatrixPieces.Builder b = firstOpMess.getAppropriatedBuilder();

        String matrixId = firstOpMess.getMatrixId();
        String chunkId = firstOpMess.getChunkId();
        String nodeId = firstOpMess.getNodeId();
        Rectangle position = firstOpMess.getArea();

        MatrixPiece rawMessage = b.buildFromTriplets(matrixId, chunkId, nodeId, tree, position, true);
        hostWorkingNode.messageSender.sendMessage(b.buildMessage(rawMessage), matrixId);
    }

    private static void updateSumTree(TreeSet<Triplet> tree, Triplet op, SemiRing semiring) {
        Triplet otherOpT = tree.tailSet(op).first();
        Object otherOp;

        if (otherOpT != null && tree.comparator().compare(op, otherOpT) == 0)
            otherOp = otherOpT.value();
        else
            otherOp = semiring.zero();

        op.setValue(semiring.add(op.value(), otherOp));

        tree.add(op);
    }

    private ArrayList<MessageMatrixValues> getMissingPieces(Operation message) {
        ArrayList<MessageMatrixValues> foundPieces = new ArrayList<MessageMatrixValues>();

        for (MessageMatrixValues chunk : chunkForOperations) {
            for (int c = message.nofMissingPieces(); c-- > 0;) {
                MatrixPieceOwnerBody piece = message.missingPiece(c);

                if (piece.getMatrixId().equals(chunk.getMatrixId())
                        && piece.getChunkId().equals(chunk.getChunkId())) {
                    foundPieces.add(chunk);
                    break;
                }
            }
        }

        return foundPieces;
    }

    void checkUpdatingState() {
        Assertion.isTrue(false, "unmplemented");
        // XXX check
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
            if (nextTriplets == null)
                for (InNodeChunk<?> inc : managedChunks) {
                    if (inc.chunk.getMatrixId().equals(matrixId)
                            && inc.chunk.doesManage(row, nextChunkColumn)) {

                        nextChunkColumn = inc.chunk.getEndCol();
                        nextTriplets = inc.accessor.matrixRowIterator(row);
                        break;
                    }
                }
        }

        private void searchMessages() {
            if (nextTriplets == null)
                for (MessageMatrixValues v : chunkForOperations) {
                    if (v.getMatrixId().equals(matrixId)
                            && v.doesManage(row, nextChunkColumn)) {

                        nextChunkColumn = v.getArea().endCol;
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

        private void findNext() {
            while (next == null && getNextIterator()) {
                tryIterator();
            }
        }

        @Override
        public boolean hasNext() {
            findNext();

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

                        nextChunkRow = v.getArea().endCol;
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

        private void findNext() {
            while (next == null && getNextIterator()) {
                tryIterator();
            }
        }

        @Override
        public boolean hasNext() {
            findNext();

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
}
