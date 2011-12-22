package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import com.rabbitmq.client.ConnectionFactory;

public class Login implements it.unipr.aotlab.dmat.core.net.Login {
    Address address;
    ConnectionFactory rabbitMQConnector = new ConnectionFactory();

    public Login(final Address address) {
        this.address = address;
        rabbitMQConnector.setPort(address.getPort());
        rabbitMQConnector.setHost(address.getHost());
    }

    public ConnectionFactory connectionFactory() {
        return rabbitMQConnector;
    }
}
