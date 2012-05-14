package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.MessageMulticastInfoWire.MessageMulticastInfoBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageImmediate;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.util.Collection;

import com.google.protobuf.ByteString;

public class MessagePrepareForMulticast extends MessageImmediate {
    MessageMulticastInfoBody.Builder builder = null;
    MessageMulticastInfoBody realBody = null;

    public MessageMulticastInfoBody body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }

        return realBody;
    }

    public MessagePrepareForMulticast(MessageMulticastInfoBody.Builder sender) {
        builder = sender;
    }

    public MessagePrepareForMulticast(String sender, int syncPort) {
        builder = MessageMulticastInfoBody.newBuilder().setSenderId(sender)
                .setSyncPort(syncPort);
    }

    MessagePrepareForMulticast(MessageMulticastInfoBody body) {
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
