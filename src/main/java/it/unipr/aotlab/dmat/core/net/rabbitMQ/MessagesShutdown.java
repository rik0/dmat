package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.net.Message;

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
    public Message parseMessage(byte[] rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageShutdown();
    }
}
