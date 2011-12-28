package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Node;

public class Nodes implements it.unipr.aotlab.dmat.core.net.Nodes {
    Node buildingNode = new Node();

    @Override
    public Nodes configureNode() {
        return this;
    }

    @Override
    public Nodes addChunck(final Chunk c) {
        buildingNode.chunks.put(c.getChunkId(), c);
        return this;
    }
 
    @Override
    public Node build() {
        final Node builtNode = buildingNode;
        reset();

        return builtNode;
    }

    @Override
    public Nodes setNodeName(final String nodeId) {
        buildingNode.nodeId = nodeId;
        return this;
    }

    @Override
    public void reset() {
        buildingNode = new Node();
    }
}
