package it.unipr.aotlab.dmat.core.net;

public abstract class MessageSupport extends Message {
    @Override
    public MessageKind messageKind() {
        return MessageKind.SUPPORT;
    }
}
