package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.errors.InvalidNode;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

public class Nodes implements it.unipr.aotlab.dmat.core.net.Nodes {
    Node buildingNode = new Node();
    NodeWorkGroup workGroup;

    public Nodes(NodeWorkGroup workGroup) {
        this.workGroup = workGroup;
    }

    @Override
    public Nodes setNodeName(String nodeId) {
        buildingNode.nodeId = nodeId;
        return this;
    }

    @Override
    public Nodes addChunck(Chunk c) {
        buildingNode.addChunk(c);
        return this;
    }

    @Override
    public void resetFactory() {
        buildingNode = new Node();
    }

    @Override
    public Nodes setNodeAddress(Address address) {
        buildingNode.address = address;
        return this;
    }

    @Override
    public Node build() throws Exception {
        validateBuildingNode();

        Node builtNode = buildingNode;
        resetFactory();

        return builtNode;
    }

    private void validateBuildingNode() throws Exception {
        buildingNode.workGroup = workGroup;

        if (buildingNode.nodeId == null)
            throw new InvalidNode("Node without ID.");

        if (buildingNode.address == null)
            throw new InvalidNode("Node without Address.");

        workGroup.registerNode(buildingNode);
    }
}
