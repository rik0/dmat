package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageShutdown;
import it.unipr.aotlab.dmat.core.registers.rabbitMQ.NodeWorkGroup;

import java.io.IOException;

public class ShutdownANode {
    public static void main(String argv[]) {
        try {
            NodeWorkGroup register = new NodeWorkGroup(new Address(), "master");
            Nodes nodes = new Nodes(register);

            Node node = nodes.setNodeName("testNode").build();

            node.sendMessage(new MessageShutdown());

            MessageSender.closeConnection();

        } catch (IdNotUnique e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
