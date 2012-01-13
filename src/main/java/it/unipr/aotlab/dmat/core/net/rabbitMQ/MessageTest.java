package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.generated.MessageTestBody;
import it.unipr.aotlab.dmat.core.generated.MessageTestBody.Body;
import it.unipr.aotlab.dmat.core.net.Message;

public class MessageTest extends Message {
    public MessageTestBody.Body body;

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
