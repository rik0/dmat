package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;

import java.io.IOException;

public class AdditionAssignment extends ShapeFriendlyOp {
    @Override
    protected void sendOrder(OrderBinaryOpBody.Builder order,
                             String computingNode)
            throws IOException {
        getNodeWorkGroup().sendOrderRaw(
                (new MessageAddAssign(order)).serialNo(serialNo),
                 computingNode);
    }
}
