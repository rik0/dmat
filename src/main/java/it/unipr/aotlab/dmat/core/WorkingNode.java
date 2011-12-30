package it.unipr.aotlab.dmat.core;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;

public class WorkingNode {
    String nodeId;
    String brokerName;
    Connector connector;
    Connection connection;

    public void connect() throws IOException {
        connection = connector.connectionFactory().newConnection();

    }

    public void consumerLoop() throws Exception {
        Channel channel = connection.createChannel();
        channel.queueDeclare(nodeId, false, true, true, null);
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(nodeId, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer
                    .nextDelivery();
        }
    }

    public WorkingNode(String nodeId, final String brokerName,
            Connector c) {
        this.nodeId = nodeId;
        this.brokerName = brokerName;
        this.connector = c;
    }
}
