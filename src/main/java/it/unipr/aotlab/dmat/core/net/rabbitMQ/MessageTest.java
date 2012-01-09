package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.AGPBCMessageTestBody.Body;

public class MessageTest extends Message {
    public AGPBCMessageTestBody.Body body;

    public MessageTest(Body body) {
        this.body = body;
    }
    
    @Override
    public void exec() {
    }

    @Override
    public byte[] message() {
        return body.toByteArray();
    }
}
