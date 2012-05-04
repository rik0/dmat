package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.messages.MessageCompare;
import it.unipr.aotlab.dmat.core.net.messages.MessageEqualityAnswer;

import java.io.IOException;

public class Compare extends ShapeFriendlyOp {
    boolean answer = true;

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
