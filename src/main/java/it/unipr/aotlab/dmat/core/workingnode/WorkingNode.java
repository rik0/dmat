package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Message.MessageKind;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Messages;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class WorkingNode {
    String nodeId;
    String brokerName;

    NodeState state;
    NodeMessageDigester digester;
    MessageSender messageSender;

    TreeSet<QueueingConsumer.Delivery> sortingBuffer
        = new TreeSet<QueueingConsumer.Delivery>(
                new Comparator<QueueingConsumer.Delivery>() {
            @Override
            public int compare(QueueingConsumer.Delivery lhs,
                               QueueingConsumer.Delivery rhs) {
                Integer lhsPri = lhs.getProperties().getPriority();
                Integer rhsPri = rhs.getProperties().getPriority();

                if (lhsPri != null && rhsPri != null)
                    return lhsPri - rhsPri;

                if (lhsPri == null)
                    return 1;

                return -1;
            }
        });

    public void consumerLoop() throws Exception {
        Channel channel = MessageSender.getConnection().createChannel();

        channel.queueDeclare(nodeId, false, false, false, null);
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(nodeId, true, queueingConsumer);

        // ask new messages
        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer
                    .nextDelivery();

            sortingBuffer.add(delivery);

            // loop over the messages that the node already has
            while ((delivery = findNextProcessableMessage()) != null) {
                Message message = Messages.readMessage(delivery);

                state.accept(digester, message);
            }
        }
    }

    private Delivery findNextProcessableMessage() {
        Iterator<Delivery> i = sortingBuffer.iterator();
        Delivery delivery = null;

        while (delivery == null && i.hasNext()) {
            Delivery possibleDelivery = i.next();
            if (isCorrectMessage(possibleDelivery)) {
                delivery = possibleDelivery;
                i.remove();
            }
        }

        return delivery;
    }

    private boolean isCorrectMessage(Delivery delivery) {
        int serialNo = delivery.getProperties().getPriority();
        int messageKind = delivery.getProperties().getDeliveryMode();
        boolean executingOrder = state.busyExecutingOrder();
        int workingOnSerialNo = state.currentOrderSerialNo;

        if (messageKind == MessageKind.IMMEDIATE.tag)
            return true;

        if (messageKind == MessageKind.ORDER.tag
                && serialNo == workingOnSerialNo
                && !executingOrder)
            return true;

        if (messageKind == MessageKind.SUPPORT.tag
                && serialNo == workingOnSerialNo
                && executingOrder)
            return true;

        return false;
    }

    public WorkingNode(String nodeId,
                       String brokerName,
                       MessageSender messageSender) {
        this.digester = new NodeMessageDigester(this);
        this.state = new NodeState(this);

        this.nodeId = nodeId;
        this.brokerName = brokerName;
        this.messageSender = messageSender;
    }

    public String getNodeId() {
        return nodeId;
    }

    static public boolean sameNode(String lhs, String rhs) {
        return lhs.equals(rhs);
    }
}
