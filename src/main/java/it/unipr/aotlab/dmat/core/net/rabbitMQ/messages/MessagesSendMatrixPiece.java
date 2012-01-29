package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.SendMatrixPiece;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesSendMatrixPiece extends Messages {
    static {
        Messages.messageFactories.put(
                MessageSendMatrixPiece.class.getSimpleName(),
                new MessagesSendMatrixPiece());
    }

    @Override
    public Message parseMessage(byte[] rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageSendMatrixPiece(
                SendMatrixPiece.Body.parseFrom(rawMessage));
    }

    private MessagesSendMatrixPiece() {
    }
}
