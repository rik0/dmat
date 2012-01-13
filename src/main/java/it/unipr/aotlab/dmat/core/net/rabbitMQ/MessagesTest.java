package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import com.google.protobuf.InvalidProtocolBufferException;

import it.unipr.aotlab.dmat.core.generated.MessageTestBody;
import it.unipr.aotlab.dmat.core.net.Message;

public class MessagesTest extends Messages {
    static {
        Messages.messageFactories.put(MessageTest.class.getSimpleName(),
                new MessagesTest());
    }

    private MessagesTest() {
    }
    
    @Override
    public Message parseMessage(byte[] rawMessage) throws InvalidProtocolBufferException {
        return new MessageTest(MessageTestBody.Body.parseFrom(rawMessage));
    }
}
