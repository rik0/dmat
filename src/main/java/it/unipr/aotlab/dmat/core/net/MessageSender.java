package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.errors.NodeNotFound;

import java.io.IOException;

public interface MessageSender {
    void sendMessage(Message m, String nodeName) throws IOException, NodeNotFound;

    void multicastMessage(Message m, Iterable<String> list) throws IOException, NodeNotFound;
}
