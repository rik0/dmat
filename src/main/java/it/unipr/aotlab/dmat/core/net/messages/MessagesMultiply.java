package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.OrderTernaryOpWire.OrderTernaryOpBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesMultiply extends Messages {
    static {
        Messages.messageFactories.put(
                MessageMultiply.class.getSimpleName(),
                new MessagesMultiply());
    }

    protected MessagesMultiply() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageMultiply(OrderTernaryOpBody.parseFrom(rawMessage));
    }
}
