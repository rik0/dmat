package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.matrices.Chunk;

public interface Nodes {
    public Nodes setNodeName(String nodeId);
    public Nodes setNodeAddress(Address address);
    public Nodes addChunck(Chunk c);

    public void resetFactory();

    Node build() throws Exception;
}
