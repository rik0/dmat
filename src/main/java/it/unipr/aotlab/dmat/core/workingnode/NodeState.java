package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssign;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceMarker;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixValues;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Operation;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;
import it.unipr.aotlab.dmat.core.semirings.SemiRings;

import java.util.ArrayList;
import java.util.List;

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

    public void eventuallyExecOperation() {
        for (Operation op : pendingOperations) {
            op.exec(this);
        }
    }

    public void exec(MessageAddAssign messageAddAssign) {
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

    private void doTheSum(ArrayList<MessageMatrixValues> missingPieces, OrderAddAssign order) {
        /*String firstMatrixId = order.getFirstAddendumMatrixId();
        String secondMatrixId = order.getSecondAddendumMatrixId();
        SemiRing<?> semiring = SemiRings.semiring(order.getType().getSemiRing());
        */
        System.err.println("---------------");
        System.err.println(order.getFirstAddendumMatrixId() + " += " + order.getSecondAddendumMatrixId());
        for (MessageMatrixValues m : missingPieces) {
            System.err.println("Id: " + m.getMatrixId() + " " + m.getArea());
        }
        System.err.println(order.getOutputPiece());
        System.err.println("=================");
    }

    private ArrayList<MessageMatrixValues> getMissingPieces(MessageAddAssign messageAddAssign) {
        ArrayList<MessageMatrixValues> foundPieces = new ArrayList<MessageMatrixValues>(); 
        
        for (MessageMatrixValues chunk : chunkForOperations) {
            for (int c = messageAddAssign.body.getMissingPiecesCount(); c-- > 0;) {
                MatrixPieceOwnerBody piece = messageAddAssign.body.getMissingPieces(c);

                if (piece.getMatrixId().equals(chunk.getMatrixId())
                        && piece.getChunkId().equals(chunk.getChunkId()))
                    foundPieces.add(chunk);
            }
        }
        
        return foundPieces;
    }
}
