package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssign;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire.TypeBody;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;

import java.io.IOException;

public class AdditionAssignment extends ShapeFriendlyOp {
    @Override
    protected void sendOperationsOrders() throws IOException {
        OrderAddAssign.Builder operation = OrderAddAssign.newBuilder();
        Matrix firstOp = operands.get(0);

        TypeBody type = TypeBody.newBuilder()
                .setElementType(firstOp.getElementType())
                .setSemiRing(firstOp.getSemiRing()).build();

        operation.setFirstAddendumMatrixId(firstOp.getMatrixId());
        operation.setSecondAddendumMatrixId(operands.get(1).getMatrixId());
        operation.setType(type);

        for (NodeWorkZonePair nodeAndworkZone : tasks) {
            Node computingNode = nodeAndworkZone.computingNode;

            MatrixPieceOwnerBody.Builder missingPiece = MatrixPieceOwnerBody.newBuilder();
            OrderAddAssignBody.Builder order = OrderAddAssignBody.newBuilder();

            for (WorkZone wz : nodeAndworkZone.workZones) {
                operation.setFirstAddendumChunkId(wz.outputChunk.getChunkId());
                operation.setFirstAddendumNodeId(wz.outputChunk.getAssignedNodeId());
                operation.setOutputPiece(wz.outputArea.convertToProto());
                order.addOperation(operation.build());

                for (NeededPieceOfChunk c : wz.involvedChunks) {
                    if ( ! computingNode.doesManage(c.chunk.getChunkId())) {
                        missingPiece.setChunkId(c.chunk.getChunkId());
                        missingPiece.setMatrixId(c.chunk.getMatrixId());
                        missingPiece.setPosition(c.piece.convertToProto());

                        order.addMissingPieces(missingPiece.build());
                    }
                }
            }

            computingNode.sendMessage(new MessageAddAssign(order.build()));
        }
    }
}
