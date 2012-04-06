package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody;
import it.unipr.aotlab.dmat.core.net.MessageSupport;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageAwaitUpdate extends MessageSupport {
    public OrderAwaitUpdateBody body;

    public MessageAwaitUpdate(OrderAwaitUpdateBody body) {
        this.body = body;
    }

    @Override
    public byte[] message() {
        return body.toByteArray();
    }

    @Override
    public void accept(NodeMessageDigester digester) {
        digester.accept(this);
    }
}
