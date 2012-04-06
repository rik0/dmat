package it.unipr.aotlab.dmat.core.registers;

import java.util.Collection;

import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.net.Node;

public interface NodeWorkGroup {
    Node getNode(String nodeId) throws NodeNotFound;
    void registerNode(Node node) throws Exception;

    int getNextOrderId();

    Collection<String> nodesId();
    Collection<Node> nodes();
}
