package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageShutdown extends Message {
    @Override
    public byte[] message() {
        return null;
    }

    @Override
    public void exec(NodeMessageDigester digester) {
        digester.accept(this);
    }
}
