package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPieceListBody;
import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOp;
import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire.TypeBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.messages.MessageCompare;
import it.unipr.aotlab.dmat.core.net.messages.MessageEqualityAnswer;

import java.io.IOException;

public class Compare extends ShapeFriendlyOp {
    boolean answer = true;
    @Override
    protected void sendOrdersToComputingNodes()
            throws IOException {
        //the same as other binary operators, a part that
        //no matrix pieces ever needs to wait for updating.
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

            OrderBinaryOpBody.Builder order = OrderBinaryOpBody.newBuilder();
            MatrixPieceListBody.Builder
                    missingPieces = MatrixPieceListBody.newBuilder();

            for (WorkZone wz : nodeAndworkZone.workZones) {
                fillInOperation(order, wz, operation);
                updateMissingPieces(missingPieces, wz, computingNode);
            }
            order.setMissingPieces(missingPieces.build());
            order.setPiecesToSend(pieces2BeSentProto(computingNode.getNodeId()));

            //no matrix pieces ever await for updating.
            order.setAwaitingUpdates(MatrixPieceListBody.getDefaultInstance());

            sendOrder(order, computingNode.getNodeId());

        }
    }
    @Override
    protected void sendOrder(OrderBinaryOpBody.Builder order, String recipientNode)
            throws IOException {
        getNodeWorkGroup().sendOrderRaw(
                (new MessageCompare(order)).serialNo(serialNo),
                 recipientNode);
    }
    @Override
    protected void awaitAnswer() {
        int nofWorkZone = tasks.size();
        System.err.println("XXX expected " + nofWorkZone + " of serialNo " + serialNo);

        try {
            while (nofWorkZone-- > 0) {
                Message m = getNodeWorkGroup().getNextAnswer(serialNo);
                MessageEqualityAnswer tm = (MessageEqualityAnswer)m;

                answer &= (tm.body().getTheInt() != 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean answer() {
        return answer;
    }
}
