package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.OrderTernaryOpWire.OrderTernaryOpBody;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;

public class MessageMultiply extends MessageTernaryOp {
    MessageMultiply(OrderTernaryOpBody body) {
        super(body);
    }

    public MessageMultiply(OrderTernaryOpBody.Builder builder) {
        super(builder);
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
