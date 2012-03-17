package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageSetMatrix extends Message {
    public OrderSetMatrixBody body;

    public MessageSetMatrix(OrderSetMatrixBody body) {
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
