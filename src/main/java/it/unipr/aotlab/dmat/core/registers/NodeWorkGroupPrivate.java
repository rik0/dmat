package it.unipr.aotlab.dmat.core.registers;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageSender;
import it.unipr.aotlab.dmat.core.net.Node;

import java.io.IOException;

public interface NodeWorkGroupPrivate {
    MessageSender getMessageSender();
    void registerNode(Node node) throws Exception;
    void sendOrderRaw(Message m, String recipient) throws IOException;
    void sendMessageToMaster(Message m);

    int getNextOrderId();
}
