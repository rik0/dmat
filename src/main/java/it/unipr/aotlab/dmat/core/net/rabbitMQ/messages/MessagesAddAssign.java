package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesAddAssign extends Messages {
    static {
        Messages.messageFactories.put(
                MessageAddAssign.class.getSimpleName(),
                new MessagesAddAssign());
    }

    private MessagesAddAssign() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageAddAssign(OrderAddAssignBody.parseFrom(rawMessage));
    }
}
