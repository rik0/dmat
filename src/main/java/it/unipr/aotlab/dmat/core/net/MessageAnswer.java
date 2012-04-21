package it.unipr.aotlab.dmat.core.net;

public abstract class MessageAnswer extends Message {
    @Override
    public MessageKind messageKind() {
        return MessageKind.ANSWER;
    }
}
