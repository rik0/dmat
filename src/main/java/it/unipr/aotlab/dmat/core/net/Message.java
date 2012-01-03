package it.unipr.aotlab.dmat.core.net;

abstract public class Message {
    public String contentType() {
        return this.getClass().getSimpleName();
    }

    public abstract byte[] message();

    public abstract void exec();
}
