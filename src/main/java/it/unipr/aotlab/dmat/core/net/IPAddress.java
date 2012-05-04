package it.unipr.aotlab.dmat.core.net;

public class IPAddress implements it.unipr.aotlab.dmat.core.net.Address {
    int port;
    String address;

    public IPAddress(String address, final int port) {
        this.port = port;
        this.address = address;
    }

    public IPAddress(String address) {
        this.address = address;
        this.port = 5672;
    }

    public IPAddress() {
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
