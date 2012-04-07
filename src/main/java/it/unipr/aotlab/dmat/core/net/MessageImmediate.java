package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;

public abstract class MessageImmediate extends Message {
    @Override
    public MessageKind messageType() {
        return MessageKind.IMMEDIATE;
    }

    @Override
    public void serialNo(int serialNo) {
        //Immediate messages always have serialNo of 0
    }

    @Override
    public int serialNo() {
        return 0;
    }
}
