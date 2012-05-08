package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

import java.util.HashMap;
import java.util.Map;

public class Node implements it.unipr.aotlab.dmat.core.net.Node {
    String nodeId;
    private Map<String, Chunk> chunks = new HashMap<String, Chunk>();
    Address address;
    NodeWorkGroup workGroup;

    @Override
    public it.unipr.aotlab.dmat.core.registers.NodeWorkGroup getWorkGroup() {
        return workGroup;
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public Chunk getChunck(String id) throws ChunkNotFound {
        Chunk chunk = chunks.get(id);
        if (chunk == null) {
            throw new ChunkNotFound();
        }

        return chunk;
    }

    @Override
    public void addChunk(Chunk c) {
        chunks.put(c.getChunkId(), c);

    }

    @Override
    public boolean doesManage(String chunkId) {
        return chunks.containsKey(chunkId);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void sendMessage(Message m) throws Exception {
        workGroup.sendMessage(m, this);
    }
}
