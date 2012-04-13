package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderDummyWire.OrderDummyBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesClearReceivedMatrixPieces extends Messages {
    static {
        Messages.messageFactories.put(
                MessageClearReceivedMatrixPieces.class.getSimpleName(),
                new MessagesClearReceivedMatrixPieces());
    }

    private MessagesClearReceivedMatrixPieces() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageClearReceivedMatrixPieces
                (OrderDummyBody.parseFrom(rawMessage));
    }
}
