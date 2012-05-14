package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.errors.NodeNotFound;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface MessageSender {
    void meetTheWorkGroup(Map<String, NodeAddress> workgroup);

    void sendMessage(Message m, String nodeName) throws IOException, NodeNotFound;

    void multicastMessage(Message m, Collection<String> list)
            throws IOException, NodeNotFound;
}
