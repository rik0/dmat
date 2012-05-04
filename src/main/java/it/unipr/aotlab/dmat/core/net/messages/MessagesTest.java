package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.MessageTestBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesTest extends Messages {
    static {
        Messages.messageFactories.put(MessageTest.class.getSimpleName(),
                new MessagesTest());
    }

    private MessagesTest() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageTest(MessageTestBody.Body.parseFrom(rawMessage));
    }
}
