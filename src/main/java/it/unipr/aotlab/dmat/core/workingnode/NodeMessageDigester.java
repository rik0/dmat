package it.unipr.aotlab.dmat.core.workingnode;

import java.io.IOException;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces.Builder;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAssignChunkToNode;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixValues;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSendMatrixPiece;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageShutdown;

public class NodeMessageDigester {
    WorkingNode hostWorkingNode;

    NodeMessageDigester(WorkingNode hostWorkingNode) {
        this.hostWorkingNode = hostWorkingNode;
    }

    private void debugMessage(Message message) {
        System.err.print("Received " + message.getClass().getCanonicalName()
                + ", ");
    }

    public void accept(MessageAssignChunkToNode message) {
        debugMessage(message);
        System.err.println("adding chunk to list of managed.");

        InNodeChunk<?> newChunk = InNodeChunks.build(hostWorkingNode,
                new Chunk(message.body));
        hostWorkingNode.state.managedChunks.add(newChunk);
    }

    public void accept(MessageShutdown message) {
        debugMessage(message);
        System.err.println("terminating.");

        throw new MainNode.Quit();
    }

    public void accept(Message message) {
        debugMessage(message);
        System.err.println("ignoring.");
    }

    public void accept(MessageMatrixValues message) {
        debugMessage(message);
        System.err.println(message.toString());

        int col = message.getColRep();
        int row = message.getRowRep();

        // TODO linear search, better solution?
        if (row != -1)
            for (InNodeChunk<?> inNodeChunk : hostWorkingNode.state.managedChunks) {
                if (message.getMatrixId()
                        .equals(inNodeChunk.chunk.getMatrixId())
                             && inNodeChunk.chunk.doesManage(row, col)) {
                    message.dispatch(inNodeChunk);
                    break;
                }
            }
    }

    public void accept(MessageSendMatrixPiece message) throws IOException {
        debugMessage(message);
        System.err.println(message.toString());

        int startRow = message.body.getNeededPiece().getStartRow();
        int endRow = message.body.getNeededPiece().getEndRow();
        int startCol = message.body.getNeededPiece().getStartCol();
        int endCol = message.body.getNeededPiece().getEndCol();
        MatrixPiece piece = null;
        
        for (InNodeChunk<?> inNodeChunk : hostWorkingNode.state.managedChunks) {
            if (message.body.getMatrixId().equals(inNodeChunk.chunk.getMatrixId())
                    && inNodeChunk.chunk.doesManage(startRow, startCol)) {
                piece = inNodeChunk.getMatrixPiece(startRow, endRow, startCol, endCol);
                break;
            }
        }
        if (piece == null)
            throw new DMatInternalError(hostWorkingNode
                    + " received and invalid request. It does not manage "
                    + message.body.getMatrixId()
                    + " row: " + startRow + " col: " + startCol);

        MatrixPieces.Builder mbuilder = MatrixPieces.matrixPiece(piece.getTag());
        hostWorkingNode.messageSender.multicastMessage(mbuilder.buildMessage(piece),  message.body.getRecipientList());
    }
    
    public void accept(MessageAddAssign message) {
        //A += B
        debugMessage(message);
        System.err.println(message.toString());
    }
}
