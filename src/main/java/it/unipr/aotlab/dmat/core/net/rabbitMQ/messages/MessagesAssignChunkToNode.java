package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesAssignChunkToNode extends Messages {
    static {
        Messages.messageFactories.put(
                MessageAssignChunkToNode.class.getSimpleName(),
                new MessagesAssignChunkToNode());
    }

    private MessagesAssignChunkToNode() {
    }

    @Override
    public Message parseMessage(byte[] rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageAssignChunkToNode(
                ChunkDescriptionWire.ChunkDescriptionBody.parseFrom(rawMessage));
    }
}
