package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageAddAssign extends Message {
    public OrderAddAssignBody body;

    public MessageAddAssign(OrderAddAssignBody body) {
        this.body = body;
    }

    @Override
    public byte[] message() {
        return body.toByteArray();
    }

    @Override
    public void exec(NodeMessageDigester digester) {
        digester.accept(this);
    }
}
