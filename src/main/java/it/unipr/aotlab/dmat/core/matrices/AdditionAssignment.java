package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssign;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire.TypeBody;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSendMatrixPiece;

import java.io.IOException;
import java.util.TreeSet;

public class AdditionAssignment extends ShapeFriendlyOp {
    @Override
    public int arity() {
        return 2;
    }

    @Override
    protected void sendOperationsOrders() throws IOException {
        OrderAddAssign.Builder operation = OrderAddAssign.newBuilder();
        Matrix firstOp = operands.get(0);

        TypeBody type = TypeBody.newBuilder().setElementType(firstOp.getElementType())
            .setSemiRing(firstOp.getSemiRing()).build();

        operation.setFirstAddendumMatrixId(firstOp.getMatrixId());
        operation.setSecondAddendumMatrixId(operands.get(1).getMatrixId());
        operation.setType(type);

        for (NodeWorkZonePair nodeAndworkZone : workers) {
            Node computingNode = nodeAndworkZone.computingNode;

            MatrixPieceOwnerBody.Builder missingMatrices = MatrixPieceOwnerBody.newBuilder();
            OrderAddAssignBody.Builder order = OrderAddAssignBody.newBuilder();

            TreeSet<String> missingChunks = new TreeSet<String>();

            for (WorkZone wz : nodeAndworkZone.workZone) {
                operation.setOutputPiece(wz.outputArea.convertToProto());
                order.addOperation(operation.build());

                for (NeededPieceOfChunk c : wz.involvedChunks) {
                    if ( ! computingNode.doesManage(c.chunk.getChunkId())
                            && missingChunks.add(c.chunk.matrixId + "." + c.chunk.chunkId)) {
                        missingMatrices.setChunkId(c.chunk.getChunkId());
                        missingMatrices.setMatrixId(c.chunk.getMatrixId());

                        order.addMissingPieces(missingMatrices.build());

                        SendMatrixPieceBody sendMatrixBody = SendMatrixPieceBody.newBuilder()
                                .setUpdate(false)
                                .setMatrixId(c.chunk.matrixId)
                                .setNeededPiece(c.piece.convertToProto())
                                .addRecipient(computingNode.getNodeId()).build();

                        getMessageSender().sendMessage(new MessageSendMatrixPiece(sendMatrixBody) , c.chunk.assignedTo);
                    }
                }
            }

            computingNode.sendMessage(new MessageAddAssign(order.build()));
        }
    }
}
