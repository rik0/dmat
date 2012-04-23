package it.unipr.aotlab.dmat.core.registers.rabbitMQ;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.net.MasterDeliveryManager;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Message.MessageKind;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class NodeWorkGroup implements it.unipr.aotlab.dmat.core.registers.NodeWorkGroupBoth {
    int orderNo = 0;
    Map<String, Node> nodes = new LinkedHashMap<String, Node>();
    MessageSender messageSender;
    String masterId;
    private QueueingConsumer queueingConsumer;
    private Channel channel;
    MasterDeliveryManager masterDeliveryManager;

    public NodeWorkGroup(Address rabbitMQaddress, String masterId)
            throws IOException {
        this.messageSender = new MessageSender(new Connector(
                        rabbitMQaddress));
        this.masterId = masterId;

        try {
            channel = MessageSender.getConnection().createChannel();

            channel.queueDeclare(masterId, false, false, false, null);
            this.queueingConsumer = new QueueingConsumer(channel);
            channel.basicConsume(masterId, true, queueingConsumer);

            registerNode(masterId);

        } catch (IdNotUnique e) {
            Assertion.isFalse(false, "Master is duplicate? WTF?");
        }

        masterDeliveryManager = new it.unipr.aotlab.dmat.core.net.rabbitMQ
                .MasterDeliveryManager(masterId);
        masterDeliveryManager.initialize();
    }

    public MessageSender messageSender() {
        return messageSender;
    }

    private void registerNode(String id) throws IdNotUnique, IOException {
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

        } finally {
            MessageSender.closeChannel(channel);
        }
    }

    @Override
    public void registerNode(Node n) throws IdNotUnique, IOException {
        String id = n.getNodeId();
        registerNode(id);
        nodes.put(id, n);
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

    @Override
    public String getMasterId() {
        return masterId;
    }

    @Override
    public void close() {
        try {
            masterDeliveryManager.close();
            channel.close();
            MessageSender.closeConnection();;
        } catch (IOException e) {
        }
    }

    @Override
    public Message getNextAnswer(int serialNo) throws Exception {
        masterDeliveryManager.setInterestedSerialNo(serialNo);
        return masterDeliveryManager.getNextDelivery();
    }
}
