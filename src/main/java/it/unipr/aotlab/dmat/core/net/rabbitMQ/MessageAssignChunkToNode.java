package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.net.Message;

public class MessageAssignChunkToNode extends Message {
    public ChunkDescription.Body body;

    public MessageAssignChunkToNode(ChunkDescription.Body body) {
        this.body = body;
    }

    @Override
    public void exec() {
    }

    @Override
    public byte[] message() {
        return body.toByteArray();
    }
}
