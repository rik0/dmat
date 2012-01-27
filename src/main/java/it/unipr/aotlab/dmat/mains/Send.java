package it.unipr.aotlab.dmat.mains;

import java.util.Hashtable;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

    private final static String Q1 = "Q1";
    private final static String Q2 = "Q2";

    public static void main(String[] argv) throws Exception {
        String message = "Hello World! ";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //during node creation, we must say to rabbitmq that it exists a new destination for message.

        //technically we are making an exchange that relays messages. a message is relayed to a queue
        //if the queue name is equals to one of the message headers.

        //user asked about a node Q1. hence we create the Q1 exchange.
        Hashtable<String, Object> Q1spec = new Hashtable<String, Object>();
        Q1spec.put(Q1, "");
        Q1spec.put("x-match", "any");
        channel.queueBind(Q1, "amq.match", "", Q1spec);

        //user asked about a node Q2
        Hashtable<String, Object> Q2spec = new Hashtable<String, Object>();
        Q2spec.put(Q2, "");
        Q2spec.put("x-match", "any");
        channel.queueBind(Q2, "amq.match", "", Q2spec);

        //we are sending a message to the queue Q1
        Hashtable<String, Object> HTonlyQ1 = new Hashtable<String, Object>();
        HTonlyQ1.put(Q1, "");
        AMQP.BasicProperties onlyQ1 = (new AMQP.BasicProperties.Builder())
                .headers(HTonlyQ1).build();
        channel.basicPublish("amq.match", "", onlyQ1, (message + Q1).getBytes());

        //we are sending a message to the queue Q2
        Hashtable<String, Object> HTonlyQ2 = new Hashtable<String, Object>();
        HTonlyQ2.put(Q2, "");
        AMQP.BasicProperties onlyQ2 = (new AMQP.BasicProperties.Builder())
                .headers(HTonlyQ2).build();
        channel.basicPublish("amq.match", "", null,
                (message + "none").getBytes());
        channel.basicPublish("amq.match", "", onlyQ2, (message + Q2).getBytes());

        //we are sending a message to the both queues
        Hashtable<String, Object> HTboth = new Hashtable<String, Object>();
        HTboth.put(Q1, "");
        HTboth.put(Q2, "");
        AMQP.BasicProperties both = (new AMQP.BasicProperties.Builder())
                .headers(HTboth).build();
        channel.basicPublish("amq.match", "", both,
                (message + Q1 + " and " + Q2).getBytes());

        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
