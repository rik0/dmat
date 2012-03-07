package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAssignChunkToNode;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageClearReceivedMatrixPieces;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixValues;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMultiply;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSendMatrixPiece;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageShutdown;

import java.io.IOException;

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
                new Chunk(message.body, hostWorkingNode.nodeId));
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

    public void accept(MessageMatrixValues message) throws IOException {
        debugMessage(message);
        System.err.println(message.toString());

        int col = message.getColRep();
        int row = message.getRowRep();

        if (message.getUpdate()) {
            // TODO linear search, better solution?
            for (InNodeChunk<?> inNodeChunk : hostWorkingNode.state.managedChunks) {
                if (message.getMatrixId().equals(
                        inNodeChunk.chunk.getMatrixId())
                        && inNodeChunk.chunk.doesManage(row, col)) {
                    message.dispatch(inNodeChunk);
                    break;
                }
            }
        }
        else {
            hostWorkingNode.state.chunkForOperations.add(message);
            hostWorkingNode.state.eventuallyExecOperation();
        }
    }

    public void accept(MessageSendMatrixPiece message) throws IOException {
        debugMessage(message);
        System.err.println(message.toString());

        Rectangle neededPiece = Rectangle.build(message.body.getNeededPiece());
        MatrixPiece piece = null;

        for (InNodeChunk<?> inNodeChunk : hostWorkingNode.state.managedChunks) {
            if (message.body.getMatrixId().equals(inNodeChunk.chunk.getMatrixId())
                    && inNodeChunk.chunk.doesManage(neededPiece.startRow, neededPiece.startCol)) {
                piece = inNodeChunk.getMatrixPiece(neededPiece, message.body.getUpdate());
                break;
            }
        }
        if (piece == null)
            throw new DMatInternalError(hostWorkingNode
                    + " received and invalid request. It does not manage "
                    + message.body.getMatrixId()
                    + " row: " + neededPiece.startRow + " col: " + neededPiece.startCol);

        MatrixPieces.Builder mbuilder = MatrixPieces.matrixPiece(piece.getTag());
        hostWorkingNode.messageSender.multicastMessage(mbuilder.buildMessage(piece),  message.body.getRecipientList());
    }

    public void accept(MessageClearReceivedMatrixPieces message) {
        debugMessage(message);
        System.err.println(message.toString());
        hostWorkingNode.state.chunkForOperations.clear();
    }
    
    // CONSIDER: only one type of message for all operations?
    public void accept(MessageAddAssign message) throws IOException {
        //A += B
        debugMessage(message);
        System.err.println(message.toString());

        hostWorkingNode.state.pendingOperations.add(message);
        hostWorkingNode.state.eventuallyExecOperation();
    }

    public void accept(MessageMultiply message) throws IOException {
        //A *= B
        debugMessage(message);
        System.err.println(message.toString());

        hostWorkingNode.state.pendingOperations.add(message);
        hostWorkingNode.state.eventuallyExecOperation();
    }
}
