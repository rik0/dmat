package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesCompare extends Messages {
    static {
        Messages.messageFactories.put(
                MessageCompare.class.getSimpleName(),
                new MessagesCompare());
    }

    protected MessagesCompare() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageCompare(OrderBinaryOpBody.parseFrom(rawMessage));
    }
}
