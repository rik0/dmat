package it.unipr.aotlab.dmat.core.registers;

import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Node;

import java.util.Collection;

public interface NodeWorkGroup {
    Node getNode(String nodeId) throws NodeNotFound;
    void sendMessage(Message m, Node recipient) throws Exception;
    String getMasterId();

    Collection<String> nodesId();
    Collection<Node> nodes();
}
