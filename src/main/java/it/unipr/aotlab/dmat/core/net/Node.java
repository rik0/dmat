package it.unipr.aotlab.dmat.core.net;

import java.io.IOException;
import java.util.Comparator;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.matrices.Chunk;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.net
 * Date: 10/18/11
 * Time: 11:06 AM
 */
public interface Node {
    static class NodeComparor implements Comparator<Node>{
        @Override 
        public int compare(Node o1, Node o2) {
            return o1.getNodeId().compareTo(o2.getNodeId());
        }
    }
    
    public String getNodeId(); 
    
    public Chunk getChunck(String id) throws ChunkNotFound;
    
    public boolean doesManage(String chunkId);

    public void sendMessage(Message m) throws IOException;
}
