package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesMatrixPieceInt32 extends Messages {
    static {
        Messages.messageFactories.put(
                MessageMatrixPieceInt32.class.getSimpleName(),
                new MessagesMatrixPieceInt32());
    }

    @Override
    public Message parseMessage(byte[] rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageMatrixPieceInt32(
                MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body.parseFrom(rawMessage));
    }

    private MessagesMatrixPieceInt32() {
    }
}
