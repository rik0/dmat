package it.unipr.aotlab.dmat.core.registers.rabbitMQ;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Message.MessageKind;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;

public class NodeWorkGroup implements it.unipr.aotlab.dmat.core.registers.NodeWorkGroupBoth {
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

    @Override
    public void sendMessage(Message m, Node recipient) throws IOException {
        switch (m.messageKind()) {
        case ORDER:
            sendMessageOrder(m, recipient);
            break;

        case IMMEDIATE:
            sendMessageImmediate(m, recipient);
            break;

        case SUPPORT:
        default:
            Assertion.isTrue(false, "Support messages should not be be sent with this method!");
        }
    }

    private void sendMessageImmediate(Message m, Node recipient) throws IOException {
        m.recipients(recipient.getNodeId());

        messageSender.sendMessage(m, recipient.getNodeId());
    }

    private void sendMessageOrder(Message m, Node recipient) throws IOException {
        m.recipients(recipient.getNodeId());
        m.serialNo(getNextOrderId());

        messageSender.multicastMessage(m, nodesId());
    }

    @Override
    public MessageSender getMessageSender() {
        return messageSender;
    }

    @Override
    public void sendOrderRaw(Message m, String recipient) throws IOException {
        Assertion.isFalse(m.serialNo() == -1,
                       "When using raw sending you have to give the serialNo!");

        Assertion.isTrue(m.messageKind() == MessageKind.ORDER,
                       "This method sends only orders!");

        m.recipients(recipient);
        messageSender.sendMessage(m, recipient);
    }
}
