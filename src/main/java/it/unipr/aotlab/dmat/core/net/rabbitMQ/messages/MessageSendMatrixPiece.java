package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageSupport;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.io.IOException;
import java.util.Collection;

public class MessageSendMatrixPiece extends MessageSupport {
    public SendMatrixPieceBody.Builder builder = null;
    private SendMatrixPieceBody realBody = null;

    MessageSendMatrixPiece(SendMatrixPieceBody body) {
        this.realBody = body;
    }

    public MessageSendMatrixPiece(SendMatrixPieceBody.Builder builder) {
        this.builder = builder;
    }

    public SendMatrixPieceBody body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }

        return realBody;
    }

    @Override
    public byte[] message() {
        return body().toByteArray();
    }

    @Override
    public void accept(NodeMessageDigester digester) throws IOException {
        digester.accept(this);
    }

    @Override
    public Message recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
        return this;
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }
}
