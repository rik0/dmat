package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPieceListBody;
import it.unipr.aotlab.dmat.core.generated.OrderTernaryOpWire.OrderTernaryOp;
import it.unipr.aotlab.dmat.core.generated.OrderTernaryOpWire.OrderTernaryOpBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire.TypeBody;
import it.unipr.aotlab.dmat.core.net.Node;

import java.io.IOException;

public abstract class TernaryOperation extends Operation {
    public abstract void sendOrder(OrderTernaryOpBody.Builder order,
                                   String nodeId) throws IOException;

    @Override
    public int arity() {
        return 3;
    }

    @Override
    protected void sendOrdersToComputingNodes()
            throws IOException {
        OrderTernaryOp.Builder operation = OrderTernaryOp.newBuilder();

        Matrix output   = operands.get(0);
        Matrix firstOp  = operands.get(1);
        Matrix secondOp = operands.get(2);

        TypeBody type = TypeBody.newBuilder()
                .setElementType(firstOp.getElementType())
                .setSemiRing(firstOp.getSemiRing()).build();

        operation.setOutputMatrixId(output.getMatrixId());
        operation.setFirstOperandMatrixId(firstOp.getMatrixId());
        operation.setSecondOperandMatrixId(secondOp.getMatrixId());
        operation.setType(type);

        for (NodeWorkZonePair nwzp : tasks) {
            Node computingNode = nwzp.computingNode;

            OrderTernaryOpBody.Builder order = OrderTernaryOpBody.newBuilder();
            MatrixPieceListBody.Builder
                missingPieces = MatrixPieceListBody.newBuilder();

            for (WorkZone wz : nwzp.workZones) {
                fillInOperation(order, wz, operation);
                updateMissingPieces(missingPieces, wz, computingNode);
            }
            order.setMissingPieces(missingPieces.build());
            order.setPiecesToSend(pieces2BeSentProto(computingNode.getNodeId()));
            order.setAwaitingUpdates(
                    awaitingUpdateProto(computingNode.getNodeId()));

            sendOrder(order, computingNode.getNodeId());
        }
    }

    @Override
    protected void sendOrdersToStorageNodes() throws IOException {
        for (String nodeId : storageNodes) {
            System.err.println("XXX storage node: " + nodeId);
            OrderTernaryOpBody.Builder order
                = OrderTernaryOpBody.newBuilder();

            order.setPiecesToSend(pieces2BeSentProto(nodeId));
            order.setAwaitingUpdates(awaitingUpdateProto(nodeId));
            order.setMissingPieces(MatrixPieceListBody
                    .getDefaultInstance());

            sendOrder(order, nodeId);
        }
    }

    private static void fillInOperation(OrderTernaryOpBody.Builder order,
            WorkZone wz,
            OrderTernaryOp.Builder operation) {
        operation.setOutputNodeId(wz.outputChunk.getAssignedNodeId());
        operation.setOutputPosition(wz.outputArea.convertToProto());

        order.addOperation(operation.build());
    }
}
