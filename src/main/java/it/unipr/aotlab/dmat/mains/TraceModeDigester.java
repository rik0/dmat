package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.messages.Messages;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class TraceModeDigester {
    private final static String QUEUE_NAME = "queue";
    private final static String EXCHANGE_NAME = "amq.rabbitmq.trace";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "deliver.#");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            EnvelopedMessageBody d = EnvelopedMessageBody.parseFrom(delivery.getBody());
            System.out.print(d.getContentType()
                               + " " + d.getSerialNo()
                               + " " + d.getMessageKind());
            Message m = Messages.readMessage(d);
            System.out.println(" Recipients: " + m.recipients());
        }
    }
}
