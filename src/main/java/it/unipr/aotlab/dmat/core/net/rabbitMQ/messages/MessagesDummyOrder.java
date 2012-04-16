package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesDummyOrder extends Messages {
    static {
        Messages.messageFactories.put(
                MessageDummyOrder.class.getSimpleName(),
                new MessagesDummyOrder());
    }

    private MessagesDummyOrder() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageShutdown();
    }
}
