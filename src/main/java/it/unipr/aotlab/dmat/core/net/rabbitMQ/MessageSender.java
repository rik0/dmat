package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import com.rabbitmq.client.ConnectionFactory;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Node;

public class MessageSender implements
        it.unipr.aotlab.dmat.core.net.MessageSender {
    ConnectionFactory rabbitMQConnector;

    public MessageSender(Connector c) {
        rabbitMQConnector = c.connectionFactory();
    }

    @Override
    public void sendMessage(Message m, final Node n) {
        // TODO Auto-generated method stub
    }

    @Override
    public void broadCastMessage(Message m) {
        // TODO Auto-generated method stub
    }
}
