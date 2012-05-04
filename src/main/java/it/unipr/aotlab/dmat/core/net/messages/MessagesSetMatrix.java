package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesSetMatrix extends Messages {
    static {
        Messages.messageFactories.put(
                MessageSetMatrix.class.getSimpleName(),
                new MessagesSetMatrix());
    }

    private MessagesSetMatrix() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageSetMatrix(OrderSetMatrixBody.parseFrom(rawMessage));
    }
}
