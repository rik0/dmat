package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Messages;

import java.util.Comparator;
import java.util.TreeSet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

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

        QueueingConsumer.Delivery delivery;
        Integer priority;
        while (true) {
            sortingBuffer.add(queueingConsumer.nextDelivery());

            delivery = sortingBuffer.first();
            priority = delivery.getProperties().getPriority();
            if (priority == null || priority == state.nextMessageNo) {
                if (priority != null)
                    ++state.nextMessageNo;

                sortingBuffer.pollFirst();

                Message m = Messages.readMessage(delivery);
                m.accept(digester);
            }
        }
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
}
