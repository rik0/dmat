package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsBytesWire;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesSetValueGeneric extends Messages {
    static {
        Messages.messageFactories.put(
                MessageSetValueGeneric.class.getSimpleName(),
                new MessagesSetValueGeneric());
    }

    @Override
    public Message parseMessage(byte[] rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageSetValueGeneric(MatrixPieceTripletsBytesWire.Body.parseFrom(rawMessage));
    }

    private MessagesSetValueGeneric() {
    }
}
