package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;



public class MessagesAddAssign extends Messages {
    static {
        Messages.messageFactories.put(
                MessageAddAssign.class.getSimpleName(),
                new MessagesAddAssign());
    }

    protected MessagesAddAssign() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageAddAssign(OrderBinaryOpBody.parseFrom(rawMessage));
    }
}
