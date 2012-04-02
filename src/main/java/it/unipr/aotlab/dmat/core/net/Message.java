package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.io.IOException;

public abstract class Message {
    public String contentType() {
        return this.getClass().getSimpleName();
    }

    public abstract byte[] message();

    public abstract void accept(NodeMessageDigester digester)
            throws IOException;
}
