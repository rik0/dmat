package it.unipr.aotlab.dmat.core.registers;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageSender;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.NodeAddress;

import java.io.IOException;
import java.util.Map;

public interface NodeWorkGroupPrivate {
    MessageSender getMessageSender();
    void registerNode(Node node) throws Exception;
    void sendOrderRaw(Message m, String recipient) throws IOException;
    Message getNextAnswer(int serialNo) throws Exception;
    Map<String, NodeAddress> nodesMap();

    int getNextOrderId();
}
