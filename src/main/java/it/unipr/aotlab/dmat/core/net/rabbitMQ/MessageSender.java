package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Node;

import java.io.IOException;
import java.util.Hashtable;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageSender implements
        it.unipr.aotlab.dmat.core.net.MessageSender {
    static ConnectionFactory rabbitMQConnector;
    static Connection connection = null;

    public static synchronized void inizializeConnection() throws IOException {
        if (connection == null) {
            connection = rabbitMQConnector.newConnection();
        }
    }

    public static synchronized void closeConnection() throws IOException {
        if (connection != null && connection.isOpen())
            connection.close();
        connection = null;
    }

    public MessageSender(Connector c) {
        rabbitMQConnector = c.connectionFactory();
    }

    public static Connection getConnection() throws IOException {
        inizializeConnection();
        return connection;
    }

    // TODO using a channel pool?
    @Override
    public void sendMessage(Message m, String destination) throws IOException {
        inizializeConnection();
        Channel channel = connection.createChannel();

        try {
            Hashtable<String, Object> recipientList = new Hashtable<String, Object>(
                    2, 1);
            recipientList.put(destination, "");

            AMQP.BasicProperties messageProperties = (new AMQP.BasicProperties.Builder())
                    .headers(recipientList).contentType(m.contentType())
                    .build();

            channel.basicPublish("amq.match", "", messageProperties,
                    m.message());

        } finally {
            closeChannel(channel);
        }
    }

    @Override
    public void multicastMessage(Message m, Iterable<String> destinations)
            throws IOException {
        inizializeConnection();
        Channel channel = connection.createChannel();

        try {
            Hashtable<String, Object> recipientList = new Hashtable<String, Object>();

            for (String nodeName : destinations)
                recipientList.put(nodeName, "");

            AMQP.BasicProperties messageProperties = (new AMQP.BasicProperties.Builder())
                    .headers(recipientList).contentType(m.contentType())
                    .build();

            channel.basicPublish("amq.match", "", messageProperties,
                    m.message());

        } finally {
            closeChannel(channel);
        }
    }

    @Override
    public void sendMessage(Message m, Node node) throws IOException {
        sendMessage(m, node.getNodeId());
    }

    public static void closeChannel(Channel channel) throws IOException {
        if (channel != null && channel.isOpen())
            channel.close();
    }
}
