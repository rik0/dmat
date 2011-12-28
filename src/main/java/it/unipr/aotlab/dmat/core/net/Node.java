package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.matrices.Chunk;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.net
 * Date: 10/18/11
 * Time: 11:06 AM
 */
public interface Node {
    public Chunk getChunck(String id) throws ChunkNotFound;

    public void sendMessage(Message m);
}
