package it.unipr.aotlab.dmat.mains;

import java.util.Hashtable;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Recv2 {
    private final static String Q1 = "Q1";
    private final static String QUEUE_NAME = "Q2";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [" + QUEUE_NAME + "] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);
        
        Hashtable<String, Object> HTonlyQ1 = new Hashtable<String, Object>();
        HTonlyQ1.put(Q1, "");
        AMQP.BasicProperties onlyQ1 = (new AMQP.BasicProperties.Builder())
                .headers(HTonlyQ1).build();
        channel.basicPublish("amq.match", "", onlyQ1, ("Message to Q1 from Q2").getBytes());


        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }
    }
}
