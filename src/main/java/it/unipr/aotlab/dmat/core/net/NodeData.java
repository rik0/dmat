package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.registers.NodeWorkGroup;

public interface NodeData {
    public Chunk getChunck(String id) throws ChunkNotFound;

    public void addChunk(Chunk c);

    public boolean doesManage(String chunkId);

    public void sendMessage(Message m) throws Exception;

    public NodeWorkGroup getWorkGroup();
}
