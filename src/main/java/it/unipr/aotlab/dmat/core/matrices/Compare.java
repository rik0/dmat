package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageCompare;

import java.io.IOException;

public class Compare extends ShapeFriendlyOp {
    @Override
    protected void sendOrder(OrderBinaryOpBody.Builder order, String recipientNode)
            throws IOException {
        getNodeWorkGroup().sendOrderRaw(
                (new MessageCompare(order)).serialNo(serialNo),
                 recipientNode);
    }

    @Override
    protected void awaitAnswer() {
        int nofWorkZone ;
        // TODO Auto-generated method stub
        super.awaitAnswer();
    }
}
