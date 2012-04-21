package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;

public class MessageCompare extends MessageBinaryOp {
    public MessageCompare(OrderBinaryOpBody.Builder builder) {
        super(builder);
    }

    protected MessageCompare(OrderBinaryOpBody body) {
        super(body);
    }

    @Override
    public void accept(NodeMessageDigester digester) throws IOException {
        digester.accept(this);
    }

    @Override
    public void exec(NodeState nodeState) throws IOException {
        nodeState.exec(this);
    }
}
