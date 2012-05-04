package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesExposeValues extends Messages {
    static {
        Messages.messageFactories.put(
                MessageExposeValues.class.getSimpleName(),
                new MessagesExposeValues());
    }

    private MessagesExposeValues() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageExposeValues(MatrixPieceOwnerBody.parseFrom(rawMessage));
    }
}
