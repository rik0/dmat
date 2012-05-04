package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.MessageSingleIntWire.MessageSingleIntBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesEqualityAnswer extends Messages {
    static {
        Messages.messageFactories.put(
                MessageEqualityAnswer.class.getSimpleName(),
                new MessagesEqualityAnswer());
    }

    protected MessagesEqualityAnswer() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageEqualityAnswer(MessageSingleIntBody.parseFrom(rawMessage));
    }
}
