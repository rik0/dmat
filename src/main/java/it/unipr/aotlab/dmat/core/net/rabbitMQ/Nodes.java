package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.InvalidNode;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Node;
import it.unipr.aotlab.dmat.core.registers.NodeRegister;

public class Nodes implements it.unipr.aotlab.dmat.core.net.Nodes {
    Node buildingNode = new Node();
    NodeRegister register;
    
    public Nodes(NodeRegister register) {
        this.register = register;
    }

    @Override
    public Nodes setConnector(Connector connector) {
        try {
            buildingNode.connector = (it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector) connector;
        } catch (ClassCastException e) {
            throw new InvalidNode("This node needs a RabbitMQ connector.");
        }
        return this;
    }

    @Override
    public Nodes addChunck(Chunk c) {
        buildingNode.Chunks().put(c.getChunkId(), c);
        return this;
    }

    @Override
    public Node build() throws IdNotUnique {
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

    void validateBuildingNode() throws IdNotUnique {
        if (buildingNode.nodeId == null)
            throw new InvalidNode("Node without ID.");
        if (buildingNode.connector == null)
            throw new InvalidNode("Node without a connector.");
        
        register.registerNode(buildingNode.nodeId, buildingNode);
    }
}
