package it.unipr.aotlab.dmat.core.registers;

import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Node;

import java.io.IOException;
import java.util.Collection;

public interface NodeWorkGroup {
    Node getNode(String nodeId) throws NodeNotFound;
    void sendMessage(Message m, Node recipients) throws Exception;
    String getMasterId();
    void close() throws IOException;

    Collection<String> nodesId();
    Collection<Node> nodes();
}
