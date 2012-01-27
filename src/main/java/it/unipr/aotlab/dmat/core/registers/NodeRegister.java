package it.unipr.aotlab.dmat.core.registers;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class NodeRegister {
    Map<String, Node> nodes = new LinkedHashMap<String, Node>();
    private Connection connector;

    public NodeRegister(Connector connector) throws IOException {
        this.connector = connector.connection();
    }

    public void registerNode(String id, Node n) throws IdNotUnique, IOException {
        if (nodes.get(id) != null)
            throw new IdNotUnique();

        Channel channel = null;
        try {
            //register in rabbitmq
            channel = connector.createChannel();
            Hashtable<String, Object> exchangeSpec = new Hashtable<String, Object>(
                    3, 1);
            exchangeSpec.put(id, "");
            exchangeSpec.put("x-match", "any");
            channel.queueBind(id, "amq.match", "", exchangeSpec);

            //register locally
            nodes.put(id, n);
        } finally {
            MessageSender.closeChannel(channel);
        }
    }

    public Node getNode(String id) throws NodeNotFound {
        Node n = nodes.get(id);
        if (n == null)
            throw new NodeNotFound();

        return n;
    }
}
