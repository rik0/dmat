package it.unipr.aotlab.dmat.core.registers;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;

public class NodeRegister {
    Map<String, Node> nodes = new LinkedHashMap<String, Node>();
    MessageSender messageSender;

    public NodeRegister(MessageSender messageSender) throws IOException {
        this.messageSender = messageSender;
    }

    public MessageSender messageSender() {
        return messageSender;
    }

    public void registerNode(Node n) throws IdNotUnique, IOException {
        String id = n.getNodeId();

        if (nodes.get(id) != null)
            throw new IdNotUnique();

        Channel channel = null;
        try {
            //register in rabbitmq
            channel = MessageSender.getConnection().createChannel();
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
