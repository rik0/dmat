package it.unipr.aotlab.dmat.core.net.rabbitMQ;

public class Address implements it.unipr.aotlab.dmat.core.net.Address {
    int port;
    String address;

    public Address(final int port, final String address) {
        this.port = port;
        this.address = address;
    }

    public Address(final String address) {
        this.address = address;
    }

    public Address() {
        this.address = "localhost";
        this.port = 5672;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getHost() {
        return address;
    }
}
