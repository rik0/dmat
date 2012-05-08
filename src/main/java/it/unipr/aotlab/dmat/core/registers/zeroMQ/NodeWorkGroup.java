package it.unipr.aotlab.dmat.core.registers.zeroMQ;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.generated.NodeWorkGroupWire.NodeDescription;
import it.unipr.aotlab.dmat.core.generated.NodeWorkGroupWire.NodeWorkGroupBody;
import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Message.MessageKind;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.messages.MessageInitializeWorkGroup;
import it.unipr.aotlab.dmat.core.net.zeroMQ.MessageSender;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.zeromq.ZMQ;

public class NodeWorkGroup implements it.unipr.aotlab.dmat.core.registers.NodeWorkGroupBoth {
    int orderNo = 1;
    Map<String, Node> nodes = new LinkedHashMap<String, Node>();
    String masterId;
    ZMQ.Context zmqContext;
    MessageSender messageSender;
    Address masterAddress;

    @Override
    public Node getNode(String nodeId) throws NodeNotFound {
        Node n = nodes.get(nodeId);
        if (n == null)
            throw new NodeNotFound();

        return n;
    }

    public NodeWorkGroup(String masterId, Address masterAddress) {
        this.zmqContext = ZMQ.context(1);
        this.masterId = masterId;
        this.masterAddress = masterAddress;
        this.messageSender = new MessageSender(this);
    }

    @Override
    public String getMasterId() {
        return masterId;
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
    public int getNextOrderId() {
        return ++orderNo;
    }

    public ZMQ.Context getSocketContext() {
        return zmqContext;
    }

    @Override
    public void close() throws IOException {
        zmqContext.term();
    }

    @Override
    public void sendMessage(Message m, Node recipient) throws Exception {
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

    private void sendMessageImmediate(Message m, Node recipient) throws Exception {
        m.recipients(recipient.getNodeId());

        messageSender.sendMessage(m, recipient.getNodeId());
    }

    private void sendMessageOrder(Message m, Node recipient) throws Exception {
        m.recipients(recipient.getNodeId());
        m.serialNo(getNextOrderId());

        messageSender.multicastMessage(m, nodesId());
    }

    @Override
    public MessageSender getMessageSender() {
        return messageSender;
    }

    @Override
    public void registerNode(Node node) throws Exception {
        String id = node.getNodeId();

        if (nodes.get(id) != null)
            throw new IdNotUnique();

        nodes.put(id, node);
    }

    @Override
    public void sendOrderRaw(Message m, String recipient) throws IOException {
        Assertion.isFalse(m.serialNo() == -1,
                "When using raw sending you have to give the serialNo!");

        Assertion.isTrue(m.messageKind() == MessageKind.ORDER,
                "This method sends only orders!");

        m.recipients(recipient);
        try {
            messageSender.sendMessage(m, recipient);
        } catch (NodeNotFound e) {
            Assertion.isTrue(false,
                    "Raw Message sent to the wrong node!");
        }
    }

    @Override
    public Message getNextAnswer(int serialNo) throws Exception {
        // TODO Auto-generated method stub
        throw new java.lang.Error("NOT IMPLEMENTED YET.");
    }

    private NodeWorkGroupBody.Builder serialize() {
        NodeWorkGroupBody.Builder thisObject = NodeWorkGroupBody.newBuilder();

        NodeDescription.Builder master = NodeDescription.newBuilder();
        master.setNodeId(getMasterId());
        master.setHost(masterAddress.getHost());
        master.setPort(masterAddress.getPort());
        thisObject.setMaster(master.build());

        for (Node n : nodes()) {
            NodeDescription.Builder description = NodeDescription.newBuilder();
            description.setNodeId(n.getNodeId());
            description.setHost(n.getAddress().getHost());
            description.setPort(n.getAddress().getPort());

            thisObject.addNodes(description.build());
        }

        return thisObject;
    }

    public void initialize() throws IOException {
        NodeWorkGroupBody.Builder b = serialize();
        for (Node n : nodes()) {
            MessageInitializeWorkGroup m = new MessageInitializeWorkGroup(b);

            m.serialNo(orderNo);
            sendOrderRaw(m, n.getNodeId());

            b = b.clone();
        }
    }

    @Override
    public Map<String, Node> nodesMap() {
        return nodes;
    }
}
