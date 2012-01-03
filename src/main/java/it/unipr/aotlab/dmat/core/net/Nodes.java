package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.matrices.Chunk;

public interface Nodes {
    public Nodes setConnector(Connector connector);
	public Nodes setNodeName(String nodeId);
    public Nodes addChunck(Chunk c);
    public void reset();

	Node build() throws IdNotUnique;
}
