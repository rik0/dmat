package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.messages.MessageShutdown;
import it.unipr.aotlab.dmat.core.net.zeroMQ.Node;
import it.unipr.aotlab.dmat.core.net.zeroMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

public class ShutdownANode {
    public static void main(String argv[]) {
        NodeWorkGroup register = new NodeWorkGroup("master", new IPAddress("192.168.0.2", 5672));
        try {
            Nodes nodes = new Nodes(register);

            Node node = nodes
                    .setNodeAddress(new IPAddress("192.168.0.2", 6000))
                    .setNodeName("testNode").build();

            register.initialize();

            node.sendMessage(new MessageShutdown());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            register.close();
        }
    }
}
