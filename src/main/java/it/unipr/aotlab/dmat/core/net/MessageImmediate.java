package it.unipr.aotlab.dmat.core.net;


public abstract class MessageImmediate extends Message {
    @Override
    public MessageKind messageType() {
        return MessageKind.IMMEDIATE;
    }

    @Override
    public Message serialNo(int serialNo) {
        //Immediate messages always have serialNo of 0
        return this;
    }

    @Override
    public int serialNo() {
        return 0;
    }
}
