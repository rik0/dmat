package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.partitions.Chunk;

public interface Nodes {
	public void configureNode();
    public void addChunck(Chunk c);

	Node makeNode();
}
