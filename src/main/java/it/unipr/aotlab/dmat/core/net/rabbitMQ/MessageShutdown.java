package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.MainNode;
import it.unipr.aotlab.dmat.core.net.Message;

public class MessageShutdown extends Message {
    @Override
    public byte[] message() {
        return null;
    }

    @Override
    public void exec() {
        System.err.println("Received " + MessageShutdown.class.getCanonicalName() + ", terminating.");
        throw new MainNode.Quit();
    }
}
