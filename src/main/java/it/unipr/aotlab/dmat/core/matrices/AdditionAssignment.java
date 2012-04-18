package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPieceListBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssign;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire.TypeBody;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.TreeSet;

public class AdditionAssignment extends ShapeFriendlyOp {
    @Override
    protected void sendOperationsOrders() throws IOException {
        TreeSet<String> unusedNodes = new TreeSet<String>();
        unusedNodes.addAll(getNodeWorkGroup().nodesId());

        sendMessagesToComputingNodes(unusedNodes);
        sendMessagesToStorageNodes(unusedNodes);
        sendMessagesToUnusedNodes(unusedNodes);
    }

    private void sendMessagesToComputingNodes(TreeSet<String> unusedNodes)
            throws IOException {
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
            boolean tr = unusedNodes.remove(computingNode.getNodeId());
            Assertion.isTrue(tr, "");

            OrderAddAssignBody.Builder order = OrderAddAssignBody.newBuilder();
            MatrixPieceListBody.Builder
                    missingPieces = MatrixPieceListBody.newBuilder();

            for (WorkZone wz : nodeAndworkZone.workZones) {
                fillInOperation(order, wz, operation);
                updateMissingPieces(missingPieces, wz, computingNode);
            }
            order.setMissingPieces(missingPieces.build());
            order.setPiecesToSend(pieces2BeSentProto(computingNode.getNodeId()));
            order.setAwaitingUpdates(awaitingUpdateProto(computingNode.getNodeId()));

            getNodeWorkGroup().sendOrderRaw(
                    (new MessageAddAssign(order)).serialNo(serialNo),
                     computingNode.getNodeId());
        }
    }

    private void sendMessagesToStorageNodes(TreeSet<String> unusedNodes)
            throws IOException {
        for (Matrix matrix : this.operands) {
            for (Chunk chunk : matrix.getChunks()) {
                String nodeId = chunk.getAssignedNode().getNodeId();
                if (unusedNodes.remove(nodeId)) {
                    System.err.println("XXX storage node: " + nodeId);
                    OrderAddAssignBody.Builder order
                        = OrderAddAssignBody.newBuilder();

                    order.setPiecesToSend(pieces2BeSentProto(nodeId));
                    order.setAwaitingUpdates(awaitingUpdateProto(nodeId));
                    order.setMissingPieces(MatrixPieceListBody
                            .getDefaultInstance());

                    getNodeWorkGroup()
                        .sendOrderRaw((new MessageAddAssign(order))
                        .serialNo(serialNo), nodeId);
                }
            }
        }
    }

    private static void fillInOperation(OrderAddAssignBody.Builder order,
                                        WorkZone wz,
                                        OrderAddAssign.Builder operation) {
        operation.setFirstAddendumNodeId(wz.outputChunk.getAssignedNodeId());
        operation.setOutputPiece(wz.outputArea.convertToProto());

        order.addOperation(operation.build());
    }
}
