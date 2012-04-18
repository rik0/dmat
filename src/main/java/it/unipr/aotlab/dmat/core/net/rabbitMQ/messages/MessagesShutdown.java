package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderDummyWire.OrderDummyBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesShutdown extends Messages {
    static {
        Messages.messageFactories.put(
                MessageShutdown.class.getSimpleName(),
                new MessagesShutdown());
    }

    private MessagesShutdown() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageShutdown(OrderDummyBody.parseFrom(rawMessage));
    }
}
