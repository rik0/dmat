package it.unipr.aotlab.dmat.core.registers.rabbitMQ;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageClearReceivedMatrixPieces;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;

public class NodeWorkGroup implements it.unipr.aotlab.dmat.core.registers.NodeWorkGroup {
    int orderNo = 0;
    Map<String, Node> nodes = new LinkedHashMap<String, Node>();
    MessageSender messageSender;

    public NodeWorkGroup(MessageSender messageSender) throws IOException {
        this.messageSender = messageSender;
    }

    public MessageSender messageSender() {
        return messageSender;
    }

    @Override
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

    @Override
    public Node getNode(String id) throws NodeNotFound {
        Node n = nodes.get(id);
        if (n == null)
            throw new NodeNotFound();

        return n;
    }

    public void clearReceivedMatrixPieces() throws IOException {
        messageSender.multicastMessage(new MessageClearReceivedMatrixPieces(), nodes.keySet());
    }

    @Override
    public int getNextOrderId() {
        return ++orderNo;
    }

    @Override
    public Collection<String> nodesId() {
        return nodes.keySet();
    }

    @Override
    public Collection<Node> nodes() {
        return nodes.values();
    }
}