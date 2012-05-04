package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire.ChunkDescriptionBody;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageOrder;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.util.Collection;

import com.google.protobuf.ByteString;

public class MessageAssignChunkToNode extends MessageOrder {
    private ChunkDescriptionBody realBody = null;
    public ChunkDescriptionBody.Builder builder = null;

    MessageAssignChunkToNode(ChunkDescriptionWire
            .ChunkDescriptionBody body) {
        this.realBody = body;
    }

    public MessageAssignChunkToNode(Chunk chunk) {
        this.builder = chunk.buildMessageBuilder();
    }

    public ChunkDescriptionBody body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }

        return realBody;
    }

    @Override
    public ByteString message() {
        return body().toByteString();
    }

    @Override
    public void accept(NodeMessageDigester digester) {
        digester.accept(this);
    }

    @Override
    public Message recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
        return this;
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }
}
