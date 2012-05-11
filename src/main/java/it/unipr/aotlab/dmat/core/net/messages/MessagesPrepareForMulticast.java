package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.MessageSingleStringWire.MessageSingleStringBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesPrepareForMulticast extends Messages {
    static {
        Messages.messageFactories.put(
                MessagePrepareForMulticast.class.getSimpleName(),
                new MessagesPrepareForMulticast());
    }

    private MessagesPrepareForMulticast() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessagePrepareForMulticast(MessageSingleStringBody.parseFrom(rawMessage));
    }
}
