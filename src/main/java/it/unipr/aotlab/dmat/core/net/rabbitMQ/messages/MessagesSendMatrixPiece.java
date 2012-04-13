package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesSendMatrixPiece extends Messages {
    static {
        Messages.messageFactories.put(
                MessageSendMatrixPiece.class.getSimpleName(),
                new MessagesSendMatrixPiece());
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageSendMatrixPiece(
                SendMatrixPieceBody.parseFrom(rawMessage));
    }

    private MessagesSendMatrixPiece() {
    }
}
