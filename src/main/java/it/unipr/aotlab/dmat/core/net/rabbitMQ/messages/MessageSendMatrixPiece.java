package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import java.io.IOException;

import it.unipr.aotlab.dmat.core.generated.SendMatrixPiece;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageSendMatrixPiece extends Message {
    public SendMatrixPiece.Body body;

    public MessageSendMatrixPiece(SendMatrixPiece.Body body) {
        this.body = body;
    }

    @Override
    public byte[] message() {
        return body.toByteArray();
    }

    @Override
    public void exec(NodeMessageDigester digester) throws IOException {
        digester.accept(this);
    }
}
 