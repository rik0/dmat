package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.matrices.Chunk;

public interface Nodes {
	public Nodes configureNode();
	public Nodes setNodeName(String nodeId);
    public Nodes addChunck(Chunk c);
    public void reset();

	Node build();
}
