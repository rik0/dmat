package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MessageSingleIntWire.MessageSingleIntBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageAnswer;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.io.IOException;
import java.util.Collection;

import com.google.protobuf.ByteString;

public class MessageEqualityAnswer extends MessageAnswer {
    MessageSingleIntBody realBody = null;
    MessageSingleIntBody.Builder builder = null;

    MessageEqualityAnswer(MessageSingleIntBody body) {
        this.realBody = body;
    }

    public MessageEqualityAnswer(MessageSingleIntBody.Builder builder) {
        this.builder = builder;
    }

    public MessageSingleIntBody body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }

        return realBody;
    }

    @Override
    public void accept(NodeMessageDigester digester) throws IOException {
        digester.accept(this);
    }

    @Override
    public ByteString message() {
        return body().toByteString();
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }

    @Override
    public Message recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
        return this;
    }
}
