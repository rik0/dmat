package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.zeroMQ.MessageReader;


public abstract class MessageImmediate extends Message {
    @Override
    public MessageKind messageKind() {
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

    public void immediateAction(MessageReader reader, EnvelopedMessageBody m) {
        reader.pushback(m);
    }
}
