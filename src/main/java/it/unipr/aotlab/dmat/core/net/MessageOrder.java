package it.unipr.aotlab.dmat.core.net;

public abstract class MessageOrder extends Message {
    @Override
    public MessageKind messageKind() {
        return MessageKind.ORDER;
    }
}
