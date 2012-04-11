package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MessageTestBody;
import it.unipr.aotlab.dmat.core.generated.MessageTestBody.Body;
import it.unipr.aotlab.dmat.core.net.MessageImmediate;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.util.Collection;

public class MessageTest extends MessageImmediate {
    private MessageTestBody.Body realBody = null;
    public MessageTestBody.Body.Builder builder = null;

    MessageTest(Body body) {
        this.realBody = body;
    }

    public MessageTestBody.Body body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }

        return realBody;
    }

    @Override
    public void accept(NodeMessageDigester digester) {
        if (digester != null)
            digester.accept(this);
    }

    @Override
    public byte[] message() {
        return body().toByteArray();
    }

    @Override
    public void recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }
}
