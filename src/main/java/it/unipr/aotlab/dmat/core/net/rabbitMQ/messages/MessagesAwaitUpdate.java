package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesAwaitUpdate extends Messages {
    static {
        Messages.messageFactories.put(
                MessageAwaitUpdate.class.getSimpleName(),
                new MessagesAwaitUpdate());
    }

    private MessagesAwaitUpdate() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageAwaitUpdate(OrderAwaitUpdateBody.parseFrom(rawMessage));
    }
}
