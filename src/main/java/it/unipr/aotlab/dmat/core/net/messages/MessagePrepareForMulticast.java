package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.MessageSingleStringWire.MessageSingleStringBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageImmediate;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.util.Collection;

import com.google.protobuf.ByteString;

public class MessagePrepareForMulticast extends MessageImmediate {
    MessageSingleStringBody.Builder builder = null;
    MessageSingleStringBody realBody = null;

    public MessageSingleStringBody body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }

        return realBody;
    }

    public MessagePrepareForMulticast(MessageSingleStringBody.Builder sender) {
        builder = sender;
    }

    public MessagePrepareForMulticast(String sender) {
        builder = MessageSingleStringBody.newBuilder().setTheString(sender);
    }

    MessagePrepareForMulticast(MessageSingleStringBody body) {
        realBody = body;
    }

    @Override
    public ByteString message() {
        return body().toByteString();
    }

    @Override
    public void accept(NodeMessageDigester digester) {
        digester.accept(this);
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
