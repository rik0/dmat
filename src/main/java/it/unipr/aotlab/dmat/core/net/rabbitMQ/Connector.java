package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import com.rabbitmq.client.ConnectionFactory;

public class Connector implements it.unipr.aotlab.dmat.core.net.Connector {
    Address address;
    ConnectionFactory rabbitMQConnector = new ConnectionFactory();

    public Connector(Address address) {
        this.address = address;
        rabbitMQConnector.setPort(address.getPort());
        rabbitMQConnector.setHost(address.getHost());
    }

    public ConnectionFactory connectionFactory() {
        return rabbitMQConnector;
    }
}
