package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Connector implements it.unipr.aotlab.dmat.core.net.Connector {
    Address address;
    ConnectionFactory rabbitMQConnector = new ConnectionFactory();
    private Connection conn;

    public Connector(Address address) {
        this.address = address;
        rabbitMQConnector.setPort(address.getPort());
        rabbitMQConnector.setHost(address.getHost());
    }

    public ConnectionFactory connectionFactory() {
        return rabbitMQConnector;
    }

    public Connection connection() throws IOException {
        if (conn == null)
            conn = connectionFactory().newConnection();
        return conn;
    }
}
