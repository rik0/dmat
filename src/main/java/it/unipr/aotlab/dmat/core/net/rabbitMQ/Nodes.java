package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.InvalidNode;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.registers.rabbitMQ.NodeWorkGroup;

import java.io.IOException;

public class Nodes implements it.unipr.aotlab.dmat.core.net.Nodes {
    Node buildingNode = new Node();
    NodeWorkGroup register;

    public Nodes(NodeWorkGroup register) {
        this.register = register;
    }

    @Override
    public Nodes addChunck(Chunk c) {
        buildingNode.getChunks().put(c.getChunkId(), c);
        return this;
    }

    @Override
    public Node build() throws IdNotUnique, IOException {
        validateBuildingNode();

        Node builtNode = buildingNode;
        resetFactory();

        return builtNode;
    }

    @Override
    public Nodes setNodeName(String nodeId) {
        buildingNode.nodeId = nodeId;
        return this;
    }

    @Override
    public void resetFactory() {
        buildingNode = new Node();
    }

    void validateBuildingNode() throws IdNotUnique, IOException {
        buildingNode.sender = register.messageSender();
        buildingNode.register = register;

        if (buildingNode.nodeId == null)
            throw new InvalidNode("Node without ID.");

        register.registerNode(buildingNode);
    }

    @Override
    public Nodes setNodeAddress(Address address) {
        // rabbitMQ nodes have no explicit address
        return this;
    }
}
