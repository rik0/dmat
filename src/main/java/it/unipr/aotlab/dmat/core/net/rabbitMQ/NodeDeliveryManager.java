package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.DeliveryManager;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Message.MessageKind;
import it.unipr.aotlab.dmat.core.net.messages.Messages;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class NodeDeliveryManager implements DeliveryManager {
    NodeState currentState;
    String nodeId;
    QueueingConsumer queueingConsumer;
    Channel channel;

    public NodeDeliveryManager(NodeState currentState, String nodeId) {
        this.currentState = currentState;
        this.nodeId = nodeId;
    }

    @Override
    public void initialize() throws IOException {
        channel = MessageSender.getConnection().createChannel();

        channel.queueDeclare(nodeId, false, false, false, null);
        queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(nodeId, true, queueingConsumer);
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    //a multiset might be better!
    ArrayList<EnvelopedMessageBody> sortingBuffer= new ArrayList<EnvelopedMessageBody>();

    @Override
    public Message getNextDelivery() throws Exception {
        EnvelopedMessageBody delivery = null;

        do {
            if ((delivery = findNextProcessableMessage()) == null) {
                EnvelopedMessageBody newDelivery = EnvelopedMessageBody
                    .parseFrom(queueingConsumer.nextDelivery().getBody());

                sortingBuffer.add(newDelivery);
            }

        } while (delivery == null);

        return Messages.readMessage(delivery);
    }

    private EnvelopedMessageBody findNextProcessableMessage() {
        Collections.sort(sortingBuffer, new Message.EnvelopedSerialComparator());

        Iterator<EnvelopedMessageBody> i = sortingBuffer.iterator();
        EnvelopedMessageBody delivery = null;

        while (delivery == null && i.hasNext()) {
            EnvelopedMessageBody possibleDelivery = i.next();
            if (isCorrectMessage(possibleDelivery)) {
                delivery = possibleDelivery;
                i.remove();
            }
        }

        return delivery;
    }

    private boolean isCorrectMessage(EnvelopedMessageBody possibleDelivery) {
        int serialNo = possibleDelivery.getSerialNo();
        int messageKind = possibleDelivery.getMessageKind();
        boolean executingOrder = currentState.busyExecutingOrder();
        int workingOnSerialNo = currentState.getCurrentSerialNo();

        if ((messageKind & ~7) != 0)
            throw new DMatInternalError("Unexpected message kind!");

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
}
