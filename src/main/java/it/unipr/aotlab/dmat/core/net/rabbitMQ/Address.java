package it.unipr.aotlab.dmat.core.net.rabbitMQ;

public class Address implements it.unipr.aotlab.dmat.core.net.Address {
    int port;
    String address;

    public Address(String address, final int port) {
        this.port = port;
        this.address = address;
    }

    public Address(String address) {
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
