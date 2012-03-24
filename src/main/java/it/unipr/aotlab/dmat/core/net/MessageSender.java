package it.unipr.aotlab.dmat.core.net;

import java.io.IOException;

public interface MessageSender {
    void sendMessage(Message m, Node node) throws IOException;
    
    void sendMessage(Message m, String nodeName, Integer no) throws IOException;
    
    void sendMessage(Message m, String nodeName) throws IOException;

    void multicastMessage(Message m, Iterable<String> list) throws IOException;
}
