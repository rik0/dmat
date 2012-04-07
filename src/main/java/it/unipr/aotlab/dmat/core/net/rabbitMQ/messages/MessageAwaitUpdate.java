package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody;
import it.unipr.aotlab.dmat.core.net.MessageSupport;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageAwaitUpdate extends MessageSupport {
    private OrderAwaitUpdateBody realBody;
    public OrderAwaitUpdateBody.Builder builder;

    MessageAwaitUpdate(OrderAwaitUpdateBody body) {
        this.realBody = body;
    }

    public MessageAwaitUpdate(OrderAwaitUpdateBody.Builder builder) {
        this.builder = builder;
    }

    public OrderAwaitUpdateBody body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }

        return realBody;
    }

    @Override
    public byte[] message() {
        return body().toByteArray();
    }

    @Override
    public void accept(NodeMessageDigester digester) {
        digester.accept(this);
    }
}
