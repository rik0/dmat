package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssign;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceMarker;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixValues;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Operation;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;
import it.unipr.aotlab.dmat.core.semirings.SemiRings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class NodeState {
    WorkingNode hostWorkingNode;
    ArrayList<InNodeChunk<?>> managedChunks = new ArrayList<InNodeChunk<?>>();
    ArrayList<MatrixPieceMarker> awaitingUpdate = new ArrayList<MatrixPieceMarker>();

    ArrayList<MessageMatrixValues> chunkForOperations = new ArrayList<MessageMatrixValues>();
    ArrayList<Operation> pendingOperations = new ArrayList<Operation>();

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
        for (Operation op : pendingOperations) {
            op.exec(this);
        }
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

    public void exec(MessageAddAssign messageAddAssign) throws IOException {
        ArrayList<MessageMatrixValues> missingPieces = getMissingPieces(messageAddAssign);
        if (missingPieces.size() == messageAddAssign.body.getMissingPiecesCount()) {
            System.err.println("Ready to do operation");
            for (OrderAddAssign order : messageAddAssign.body.getOperationList()) {
                doTheSum(missingPieces, order);
            }
        }
        else {
            System.err.println("Still missing pieces for operation " + messageAddAssign.toString());
        }
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

    private ArrayList<MessageMatrixValues> getMissingPieces(MessageAddAssign messageAddAssign) {
        ArrayList<MessageMatrixValues> foundPieces = new ArrayList<MessageMatrixValues>();

        for (MessageMatrixValues chunk : chunkForOperations) {
            for (int c = messageAddAssign.body.getMissingPiecesCount(); c-- > 0;) {
                MatrixPieceOwnerBody piece = messageAddAssign.body.getMissingPieces(c);

                if (piece.getMatrixId().equals(chunk.getMatrixId())
                        && piece.getChunkId().equals(chunk.getChunkId())) {
                    foundPieces.add(chunk);
                    break;
                }
            }
        }

        return foundPieces;
    }
}
