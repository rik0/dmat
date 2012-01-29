package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesMatrixPiece extends Messages {
    static {
        Messages.messageFactories.put(
                MessageSendMatrixPiece.class.getSimpleName(),
                new MessagesMatrixPiece());
    }

    @Override
    public Message parseMessage(byte[] rawMessage)
            throws InvalidProtocolBufferException {
        throw new DMatInternalError("unplemented factory!");
    }

    private MessagesMatrixPiece() {
    }
}
