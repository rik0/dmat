package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPieceListBody;
import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOp;
import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire.TypeBody;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.TreeSet;


public abstract class BinaryOperation extends Operation {
    protected abstract void sendOrder(OrderBinaryOpBody.Builder order,
                                      String recipientNode) throws IOException;

    @Override
    public int arity() {
        return 2;
    }

    @Override
    protected void sendOperationsOrders() throws IOException {
        TreeSet<String> unusedNodes = new TreeSet<String>();
        unusedNodes.addAll(getNodeWorkGroup().nodesId());

        sendMessagesToComputingNodes(unusedNodes);
        sendMessagesToStorageNodes(unusedNodes);
        sendMessagesToUnusedNodes(unusedNodes);
    }

    protected void sendMessagesToComputingNodes(TreeSet<String> unusedNodes)
            throws IOException {
        OrderBinaryOp.Builder operation = OrderBinaryOp.newBuilder();

        Matrix firstOp = operands.get(0);

        TypeBody type = TypeBody.newBuilder()
                .setElementType(firstOp.getElementType())
                .setSemiRing(firstOp.getSemiRing()).build();

        operation.setFirstOperandMatrixId(firstOp.getMatrixId());
        operation.setSecondOperandMatrixId(operands.get(1).getMatrixId());
        operation.setType(type);

        for (NodeWorkZonePair nodeAndworkZone : tasks) {
            Node computingNode = nodeAndworkZone.computingNode;
            boolean tr = unusedNodes.remove(computingNode.getNodeId());
            Assertion.isTrue(tr, "");

            OrderBinaryOpBody.Builder order = OrderBinaryOpBody.newBuilder();
            MatrixPieceListBody.Builder
                    missingPieces = MatrixPieceListBody.newBuilder();

            for (WorkZone wz : nodeAndworkZone.workZones) {
                fillInOperation(order, wz, operation);
                updateMissingPieces(missingPieces, wz, computingNode);
            }
            order.setMissingPieces(missingPieces.build());
            order.setPiecesToSend(pieces2BeSentProto(computingNode.getNodeId()));
            order.setAwaitingUpdates(awaitingUpdateProto(computingNode.getNodeId()));

            sendOrder(order, computingNode.getNodeId());

        }
    }

    private static void fillInOperation(OrderBinaryOpBody.Builder order,
            WorkZone wz,
            OrderBinaryOp.Builder operation) {
        operation.setFirstOperandNodeId(wz.outputChunk.getAssignedNodeId());
        operation.setOutputPosition(wz.outputArea.convertToProto());

        order.addOperation(operation.build());
    }

    protected void sendMessagesToStorageNodes(TreeSet<String> unusedNodes)
            throws IOException {
        for (Matrix matrix : this.operands) {
            for (Chunk chunk : matrix.getChunks()) {
                String nodeId = chunk.getAssignedNode().getNodeId();
                if (unusedNodes.remove(nodeId)) {
                    System.err.println("XXX storage node: " + nodeId);
                    OrderBinaryOpBody.Builder order
                        = OrderBinaryOpBody.newBuilder();

                    order.setPiecesToSend(pieces2BeSentProto(nodeId));
                    order.setAwaitingUpdates(awaitingUpdateProto(nodeId));
                    order.setMissingPieces(MatrixPieceListBody
                            .getDefaultInstance());

                    sendOrder(order, nodeId);
                }
            }
        }
    }
}
