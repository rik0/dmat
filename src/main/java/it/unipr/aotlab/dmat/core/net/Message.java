package it.unipr.aotlab.dmat.core.net;

import java.io.IOException;

import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

abstract public class Message {
    public String contentType() {
        return this.getClass().getSimpleName();
    }

    public abstract byte[] message();

    public abstract void exec(NodeMessageDigester digester) throws IOException;
}
