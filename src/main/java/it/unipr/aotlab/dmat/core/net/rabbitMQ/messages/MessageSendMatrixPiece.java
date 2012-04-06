package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire;
import it.unipr.aotlab.dmat.core.net.MessageSupport;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.io.IOException;

public class MessageSendMatrixPiece extends MessageSupport {
    public SendMatrixPieceWire.SendMatrixPieceBody body;

    public MessageSendMatrixPiece(SendMatrixPieceWire.SendMatrixPieceBody body) {
        this.body = body;
    }

    @Override
    public byte[] message() {
        return body.toByteArray();
    }

    @Override
    public void accept(NodeMessageDigester digester) throws IOException {
        digester.accept(this);
    }
}
