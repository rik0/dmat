package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.registers.rabbitMQ.NodeWorkGroup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Node implements it.unipr.aotlab.dmat.core.net.Node {
    int messageNo = 0;
    String nodeId;
    private Map<String, Chunk> chunks;
    MessageSender sender;
    NodeWorkGroup register;

    Node() {
    }

    Map<String, Chunk> getChunks() {
        if (chunks == null)
            chunks = new HashMap<String, Chunk>();

        return chunks;
    }

    @Override
    public Chunk getChunck(String id) throws ChunkNotFound {
        Chunk chunk = getChunks().get(id);
        if (chunk == null) {
            throw new ChunkNotFound();
        }

        return chunk;
    }

    @Override
    public void sendMessage(Message m) throws IOException {
        register.sendMessage(m, this);
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public boolean doesManage(String chunkId) {
        return getChunks().containsKey(chunkId);
    }

    @Override
    public void addChunk(Chunk c) {
        getChunks().put(c.getChunkId(), c);
    }

    @Override
    public NodeWorkGroup getWorkGroup() {
        return register;
    }
}
