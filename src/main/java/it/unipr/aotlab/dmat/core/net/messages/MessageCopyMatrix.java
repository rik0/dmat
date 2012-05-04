package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;

public class MessageCopyMatrix extends MessageBinaryOp {
    public MessageCopyMatrix(OrderBinaryOpBody.Builder builder) {
        super(builder);
    }

    protected MessageCopyMatrix(OrderBinaryOpBody body) {
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
