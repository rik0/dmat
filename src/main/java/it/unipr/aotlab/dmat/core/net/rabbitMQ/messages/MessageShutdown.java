package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.net.MessageOrder;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageShutdown extends MessageOrder {
    @Override
    public byte[] message() {
        return null;
    }

    @Override
    public void accept(NodeMessageDigester digester) {
        digester.accept(this);
    }
}
