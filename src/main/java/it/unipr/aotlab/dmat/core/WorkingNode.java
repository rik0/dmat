package it.unipr.aotlab.dmat.core;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Messages;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class WorkingNode {
    String nodeId;
    String brokerName;
    Connector connector;
    Connection connection;

    public void connect() throws IOException {
        connection = connector.connection();
    }

    public void consumerLoop() throws Exception {
        Channel channel = connection.createChannel();
        channel.queueDeclare(nodeId, false, false, false, null);
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(nodeId, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer
                    .nextDelivery();
            Message m = Messages.readMessage(delivery);
            m.exec();
        }
    }

    public WorkingNode(String nodeId, final String brokerName, Connector c) {
        this.nodeId = nodeId;
        this.brokerName = brokerName;
        this.connector = c;
    }
}
