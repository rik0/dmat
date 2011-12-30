package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.net.Message;

public class MessagesTest extends Messages {
    static {
        Messages.messageFactories.put(MessageTest.class.getSimpleName(),
                new MessagesTest());
    }

    private MessagesTest() {
    }

    @Override
    public Message parseMessage(byte[] rawMessage) {
        return super.parseMessage(rawMessage);
    }
}
