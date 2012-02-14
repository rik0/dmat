package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageAssignChunkToNode extends Message {
    public ChunkDescriptionWire.ChunkDescriptionBody body;

    public MessageAssignChunkToNode(ChunkDescriptionWire.ChunkDescriptionBody body) {
        this.body = body;
    }

    public MessageAssignChunkToNode(Chunk chunk) {
        this.body = chunk.buildMessageBody();
    }

    @Override
    public byte[] message() {
        return body.toByteArray();
    }

    @Override
    public void exec(NodeMessageDigester digester) {
        digester.accept(this);
    }
}
