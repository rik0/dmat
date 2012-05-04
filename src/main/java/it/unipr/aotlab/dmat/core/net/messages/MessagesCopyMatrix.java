package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesCopyMatrix extends Messages {
    static {
        Messages.messageFactories.put(
                MessageCopyMatrix.class.getSimpleName(),
                new MessagesCopyMatrix());
    }

    protected MessagesCopyMatrix() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageCopyMatrix(OrderBinaryOpBody.parseFrom(rawMessage));
    }
}
