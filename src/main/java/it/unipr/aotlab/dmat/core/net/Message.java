package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.io.IOException;

public abstract class Message {
    public enum MessageKind {
        Immediate(0x0),
        Operation(0x1),
        Order(0x2);

        public int tag;
        private MessageKind(int tag) {
            this.tag = tag;
        }
    };

    public String contentType() {
        return this.getClass().getSimpleName();
    }

    public MessageKind messageType() {
        return MessageKind.Order;
    }

    public abstract byte[] message();

    public abstract void accept(NodeMessageDigester digester)
            throws IOException;
}
