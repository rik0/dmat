package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.NodeAddress;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

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

    @Override
    public void multicastMessage(Message m, Iterable<String> destinations)
            throws IOException {
        inizializeConnection();
        Channel channel = connection.createChannel();

        EnvelopedMessageBody envelopedMessage = EnvelopedMessageBody.newBuilder()
                .setSerialNo(m.serialNo())
                .setContentType(m.contentType())
                .setMessageKind(m.messageKind().tag)
                .setMessage(m.message()).build();

        try {
            Hashtable<String, Object> recipientList
                = new Hashtable<String, Object>();

            for (String nodeName : destinations)
                recipientList.put(nodeName, "");

            AMQP.BasicProperties messageProperties
                     = (new AMQP.BasicProperties.Builder())
                        .headers(recipientList)
                        .build();

            channel.basicPublish("amq.match",
                                 "",
                                 messageProperties,
                                 envelopedMessage.toByteArray());

        } finally {
            closeChannel(channel);
        }
    }

    public static void closeChannel(Channel channel) throws IOException {
        if (channel != null && channel.isOpen())
            channel.close();
    }

    // TODO using a channel pool?
    @Override
    public void sendMessage(Message m,
                            String destination)
            throws IOException {
        inizializeConnection();

        Channel channel = connection.createChannel();

        EnvelopedMessageBody envelopedMessage = EnvelopedMessageBody.newBuilder()
            .setSerialNo(m.serialNo())
            .setContentType(m.contentType())
            .setMessageKind(m.messageKind().tag)
            .setMessage(m.message()).build();

        try {
            Hashtable<String, Object> recipientList
                = new Hashtable<String, Object>(2, 1);
            recipientList.put(destination, "");

            AMQP.BasicProperties messageProperties
                = (new AMQP.BasicProperties.Builder())
                    .headers(recipientList)
                    .build();

            channel.basicPublish("amq.match",
                                 "",
                                 messageProperties,
                                 envelopedMessage.toByteArray());
        } finally {
            closeChannel(channel);
        }
    }

    @Override
    public void meetTheWorkGroup(Map<String, NodeAddress> workgroup) {
        throw new DMatInternalError("rabbitMQ senders do not need to know"
                + " the workgroup.");
    }
}
