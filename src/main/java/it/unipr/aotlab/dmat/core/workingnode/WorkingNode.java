package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Messages;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class WorkingNode {
    String nodeId;
    String brokerName;

    NodeState state;
    NodeMessageDigester digester;
    MessageSender messageSender;

    public void consumerLoop() throws Exception {
        Channel channel = MessageSender.getConnection().createChannel();

        channel.queueDeclare(nodeId, false, false, false, null);
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(nodeId, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer
                    .nextDelivery();
            Message m = Messages.readMessage(delivery);
            m.exec(digester);
        }
    }

    public WorkingNode(String nodeId, final String brokerName, MessageSender messageSender) {
        this.digester = new NodeMessageDigester(this);
        this.state = new NodeState(this);

        this.nodeId = nodeId;
        this.brokerName = brokerName;
        this.messageSender = messageSender;
    }
}
