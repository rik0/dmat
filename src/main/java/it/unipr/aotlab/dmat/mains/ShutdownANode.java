package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageShutdown;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.NodeRegister;

import java.io.IOException;

public class ShutdownANode {
    public static void main(String argv[]) {
        NodeRegister register = new NodeRegister();
        Nodes nodes = new Nodes(register);

        Connector connector = new Connector(new Address("127.0.0.1"));

        Node node;
        try {
            node = nodes.setNodeName("testNode").setConnector(connector)
                    .build();
            node.sendMessage(new MessageShutdown());
            connector.connection().close();
        } catch (IdNotUnique e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
