package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.generated.NodeWorkGroupWire.NodeDescription;
import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceListWire.SendMatrixPiece;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.NodeAddress;
import it.unipr.aotlab.dmat.core.net.messages.MessageAddAssign;
import it.unipr.aotlab.dmat.core.net.messages.MessageAssignChunkToNode;
import it.unipr.aotlab.dmat.core.net.messages.MessageClearReceivedMatrixPieces;
import it.unipr.aotlab.dmat.core.net.messages.MessageCompare;
import it.unipr.aotlab.dmat.core.net.messages.MessageCopyMatrix;
import it.unipr.aotlab.dmat.core.net.messages.MessageDummyOrder;
import it.unipr.aotlab.dmat.core.net.messages.MessageExposeValues;
import it.unipr.aotlab.dmat.core.net.messages.MessageInitializeWorkGroup;
import it.unipr.aotlab.dmat.core.net.messages.MessageMatrixValues;
import it.unipr.aotlab.dmat.core.net.messages.MessageMultiply;
import it.unipr.aotlab.dmat.core.net.messages.MessagePrepareForMulticast;
import it.unipr.aotlab.dmat.core.net.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.net.messages.MessageShutdown;
import it.unipr.aotlab.dmat.core.net.messages.Operation;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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
                new Chunk(message.body(), hostWorkingNode.nodeId));
        hostWorkingNode.state.managedChunks.add(newChunk);

        hostWorkingNode.state.orderDone();
    }

    public void accept(MessageShutdown message) {
        debugMessage(message);
        System.err.println("terminating.");

        throw new MainNode.Quit();
        //do not expect much after this
    }

    public void accept(MessageInitializeWorkGroup message) {
        debugMessage(message);
        System.err.println("Initializing.");

        Map<String, NodeAddress> map = new LinkedHashMap<String, NodeAddress>();

        NodeDescription masterDesc = message.body().getMaster();
        String id = masterDesc.getNodeId();
        String host = masterDesc.getHost();
        int port = masterDesc.getPort();

        map.put(id, new NodeInfo(id, host, port));

        for (int i = message.body().getNodesCount(); i-- > 0;) {
            NodeDescription nodeDesc = message.body().getNodes(i);

            id = nodeDesc.getNodeId();
            host = nodeDesc.getHost();
            port = nodeDesc.getPort();

            map.put(id, new NodeInfo(id, host, port));
        }

        hostWorkingNode.messageSender.meetTheWorkGroup(map);
        hostWorkingNode.state.orderDone();
    }

    public void accept(MessageDummyOrder message) {
        debugMessage(message);
        System.err.println("Easy. Done.");
        hostWorkingNode.state.orderDone();
    }

    public void accept(Message message) {
        debugMessage(message);
        System.err.println("ignoring.");
    }

    public void accept(MessageMatrixValues message) throws IOException {
        debugMessage(message);
        System.err.println("");

        if (message.getUpdate()) {
            hostWorkingNode.state.chunkForUpdating.add(message);
            hostWorkingNode.state.checkUpdatingState();
        }
        else {
            hostWorkingNode.state.chunkForOperations.add(message);
            hostWorkingNode.state.eventuallyExecOperation();
        }
    }

    private void sendMatrixPieces(Operation operation) throws IOException {
        for (int i = operation.nofPiecesToBeSent(); i-- > 0;) {
            SendMatrixPiece piece2beSent = operation.pieceToBeSent(i);
            Rectangle neededPiece = Rectangle.build(piece2beSent.getNeededPiece());
            MatrixPiece piece = null;

            for (InNodeChunk<?> inNodeChunk : hostWorkingNode.state.managedChunks) {
                if (piece2beSent.getMatrixId().equals(inNodeChunk.chunk.getMatrixId())
                        && inNodeChunk.chunk.doesManage(neededPiece.startRow, neededPiece.startCol)) {
                    piece = inNodeChunk.getMatrixPiece(neededPiece, false);
                    break;
                }
            }
            Assertion.isTrue(piece != null, hostWorkingNode
                    + " does not manage " + piece2beSent.getMatrixId() + "("
                    + neededPiece.startRow +", " + neededPiece.startCol + ")");

            MatrixPieces.Builder mbuilder = MatrixPieces
                    .matrixPiece(piece.getTag());

            Message mess = mbuilder.buildMessage(piece)
                                   .serialNo(operation.serialNo());
            mess.recipients(piece2beSent.getRecipientList());

            try {
                System.err.println("XXX message digester 143 nof recipients " + piece2beSent.getRecipientList().size());

                hostWorkingNode.messageSender
                        .multicastMessage(mess,
                                          piece2beSent.getRecipientList());
            } catch (NodeNotFound e) {
                throw new DMatInternalError("Sending matrix piece to a non-existing node.");
            }
        }
    }

    public void accept(MessageClearReceivedMatrixPieces message) {
        debugMessage(message);
        System.err.println(message.toString());
        hostWorkingNode.state.chunkForOperations.clear();

        hostWorkingNode.state.orderDone();
    }

    private void operationCommonWork(Operation message) throws IOException {
        System.err.println("XXX BRE");
        for (int i = hostWorkingNode.state.awaitingUpdate.size(); i-- > 0;) {
            it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPiece p = hostWorkingNode.state.awaitingUpdate.get(i);
            System.err.println("XXX " + p.getMatrixId());
            System.err.println("XXX " + p.getPosition());
        }
        System.err.println("XXX ERB");
        Assertion.isTrue(hostWorkingNode.state.awaitingUpdate.isEmpty(),
                "Unresolved updating!");

        sendMatrixPieces(message);

        hostWorkingNode.state.nofAwaitingUpdate
            = message.nofPiacesAwaitingUpdate();

        for (int i = message.nofPiacesAwaitingUpdate(); i-- > 0;)
            hostWorkingNode.state.awaitingUpdate.add(message.awaitingUpdate(i));
    }

    public void accept(MessageAddAssign message) throws IOException {
        //A += B
        debugMessage(message);
        System.err.println(message.toString());
        operationCommonWork(message);

        hostWorkingNode.state.pendingOperations.add(message);
        hostWorkingNode.state.eventuallyExecOperation();
    }

    public void accept(MessageMultiply message) throws IOException {
        //A = B * C
        debugMessage(message);
        System.err.println(message.toString());
        operationCommonWork(message);

        hostWorkingNode.state.pendingOperations.add(message);
        hostWorkingNode.state.eventuallyExecOperation();
    }

    public void accept(MessageCopyMatrix message) throws IOException {
        //A = B
        debugMessage(message);
        System.err.println(message.toString());
        operationCommonWork(message);

        hostWorkingNode.state.pendingOperations.add(message);
        hostWorkingNode.state.eventuallyExecOperation();
    }

    public void accept(MessageCompare message) throws IOException {
        //A == B
        debugMessage(message);
        System.err.println(message.toString());
        operationCommonWork(message);

        hostWorkingNode.state.pendingOperations.add(message);
        hostWorkingNode.state.eventuallyExecOperation();
    }

    public void accept(MessageSetMatrix message) {
        debugMessage(message);
        System.err.println(message.toString());

        hostWorkingNode.state.updateMatrix(message);
        hostWorkingNode.state.orderDone();
    }

    public void accept(MessagePrepareForMulticast message) {
        debugMessage(message);
        System.err.println(message.toString());

        String sender = message.body().getSenderId();
        String syncPort = Integer.toString(message.body().getSyncPort());

        hostWorkingNode.deliveryManager
            .prepareForReceivingMulticast(sender, syncPort);
    }

    public void accept(MessageExposeValues message) {
        debugMessage(message);
        System.err.println(message.toString());
        InNodeChunk<?> chunk = hostWorkingNode.state.getChunk(message.body().getMatrixId(), message.body().getChunkId());
        if (chunk == null) {
            System.err.println("This node knows nothing about " + message.body().getMatrixId() + "." + message.body().getChunkId());
            return;
        }

        Iterator<Triplet> i = chunk.accessor.matrixPieceIterator(null);
        System.err.println(message.body().getMatrixId() + "." + message.body().getChunkId() + ":");
        while (i.hasNext()) {
            Triplet t = i.next();
            System.err.println((t.row() + 1) + " " + (t.col() + 1) + " " + t.value());
        }

        hostWorkingNode.state.orderDone();
    }
}
