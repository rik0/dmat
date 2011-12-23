package it.unipr.aotlab.dmat.core.net;

public interface MessageSender {
    void sendMessage(Message m, Node n);
    void broadCastMessage(Message m);
}
