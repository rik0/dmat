package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MessageTestBody;
import it.unipr.aotlab.dmat.core.generated.MessageTestBody.Body;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageImmediate;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.util.Collection;

import com.google.protobuf.ByteString;

public class MessageTest extends MessageImmediate {
    private MessageTestBody.Body realBody = null;
    public MessageTestBody.Body.Builder builder = null;

    MessageTest(Body body) {
        this.realBody = body;
    }

    public MessageTest(MessageTestBody.Body.Builder builder) {
        this.builder = builder;
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
    public ByteString message() {
        return body().toByteString();
    }

    @Override
    public Message recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
        return this;
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }
}
