package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.InvalidNode;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.registers.NodeRegister;

import java.io.IOException;

public class Nodes implements it.unipr.aotlab.dmat.core.net.Nodes {
    Node buildingNode = new Node();
    NodeRegister register;

    public Nodes(NodeRegister register) {
        this.register = register;
    }

    @Override
    public Nodes addChunck(Chunk c) {
        buildingNode.Chunks().put(c.getChunkId(), c);
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

        if (buildingNode.nodeId == null)
            throw new InvalidNode("Node without ID.");

        register.registerNode(buildingNode);
    }
}
