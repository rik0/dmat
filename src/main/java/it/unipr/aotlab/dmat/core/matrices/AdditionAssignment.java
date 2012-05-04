package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.net.messages.MessageAddAssign;

import java.io.IOException;

public class AdditionAssignment extends ShapeFriendlyOp {
    @Override
    protected void sendOrder(OrderBinaryOpBody.Builder order,
                             String recipientNode)
            throws IOException {
        getNodeWorkGroup().sendOrderRaw(
                (new MessageAddAssign(order)).serialNo(serialNo),
                 recipientNode);
    }
}
