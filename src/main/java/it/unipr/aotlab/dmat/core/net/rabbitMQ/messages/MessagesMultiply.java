package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesMultiply extends Messages {
    static {
        Messages.messageFactories.put(
                MessageMultiply.class.getSimpleName(),
                new MessagesMultiply());
    }

    private MessagesMultiply() {
    }

    @Override
    public Message parseMessage(byte[] rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageAddAssign(OrderAddAssignBody.parseFrom(rawMessage));
    }
}
