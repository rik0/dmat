package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import java.util.HashMap;
import java.util.Map;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Message;

public class Node implements it.unipr.aotlab.dmat.core.net.Node {
    String nodeId;
    Map<String, Chunk> chunks = new HashMap<String, Chunk>();

    @Override
    public Chunk getChunck(String id) throws ChunkNotFound {
        Chunk chunk = chunks.get(id);
        if (chunk == null) {
            throw new ChunkNotFound();
        }
        
        return chunk;
    }

    @Override
    public void sendMessage(Message m) {
        // TODO Auto-generated method stub
    }
}
