package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Message.MessageKind;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class WorkingNode {
    String nodeId;
    String brokerName;

    NodeState state;
    NodeMessageDigester digester;
    MessageSender messageSender;

    //much better a multiset!
    ArrayList<EnvelopedMessageBody> sortingBuffer= new ArrayList<EnvelopedMessageBody>();

    public void consumerLoop() throws Exception {
        Channel channel = MessageSender.getConnection().createChannel();

        channel.queueDeclare(nodeId, false, false, false, null);
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(nodeId, true, queueingConsumer);

        // ask new messages
        while (true) {
            EnvelopedMessageBody delivery = EnvelopedMessageBody
                        .parseFrom(queueingConsumer.nextDelivery().getBody());

            System.err.println("XXX ARRIVED  " +
            " ContentType: "  + delivery.getContentType()
            + " SerialNo: " + delivery.getSerialNo()
            + " MessageKind: " + delivery.getMessageKind()
            + " amIBusy? " + state.busyExecutingOrder()
            + " Working on: " + state.currentOrderSerialNo);

            sortingBuffer.add(delivery);

            // loop over the messages that the node already has
            while ((delivery = findNextProcessableMessage()) != null) {
                Message message = Messages.readMessage(delivery);

                state.accept(digester, message);
            }
        }
    }

    private EnvelopedMessageBody findNextProcessableMessage() {
        Collections.sort(sortingBuffer, new Comparator<EnvelopedMessageBody>() {
            @Override
            public int compare(EnvelopedMessageBody lhs, EnvelopedMessageBody rhs) {
                return lhs.getSerialNo() - rhs.getSerialNo();
            }
        });

        Iterator<EnvelopedMessageBody> i = sortingBuffer.iterator();
        EnvelopedMessageBody delivery = null;

        while (delivery == null && i.hasNext()) {
            EnvelopedMessageBody possibleDelivery = i.next();
            if (isCorrectMessage(possibleDelivery)) {
                delivery = possibleDelivery;
                System.err.println("XXX DIGESTED " +
                        " ContentType: "  + delivery.getContentType()
                        + " SerialNo: " + delivery.getSerialNo()
                        + " MessageKind: " + delivery.getMessageKind()
                        + " amIBusy? " + state.busyExecutingOrder()
                        + " Working on: " + state.currentOrderSerialNo);
                i.remove();
            }
        }

        return delivery;
    }

    private boolean isCorrectMessage(EnvelopedMessageBody possibleDelivery) {
        int serialNo = possibleDelivery.getSerialNo();
        int messageKind = possibleDelivery.getMessageKind();
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
