package it.unipr.aotlab.dmat.core.net;

import java.io.IOException;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.matrices.Chunk;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.net
 * Date: 10/18/11
 * Time: 11:06 AM
 */
public interface Node {
    public String getNodeId(); 
    
    public Chunk getChunck(String id) throws ChunkNotFound;

    public void sendMessage(Message m) throws IOException;
}
