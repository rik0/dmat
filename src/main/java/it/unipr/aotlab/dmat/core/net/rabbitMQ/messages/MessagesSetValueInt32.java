package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesSetValueInt32 extends Messages {
    static {
        Messages.messageFactories.put(
                MessageSetValueInt32.class.getSimpleName(),
                new MessagesSetValueInt32());
    }

    @Override
    public Message parseMessage(byte[] rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageSetValueInt32(
                MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body.parseFrom(rawMessage));
    }

    private MessagesSetValueInt32() {
    }
}
