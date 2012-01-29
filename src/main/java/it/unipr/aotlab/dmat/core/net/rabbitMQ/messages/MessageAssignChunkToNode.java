package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageAssignChunkToNode extends Message {
    public ChunkDescription.Body body;

    public MessageAssignChunkToNode(ChunkDescription.Body body) {
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
