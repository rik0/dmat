package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.net.MessageOrder;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageExposeValues extends MessageOrder {
    public MatrixPieceOwnerBody.Builder builder = null;
    private MatrixPieceOwnerBody realBody = null;

    MessageExposeValues(MatrixPieceOwnerBody body) {
        this.realBody = body;
    }

    public MessageExposeValues(MatrixPieceOwnerBody.Builder builder) {
        this.builder = builder;
    }

    public MatrixPieceOwnerBody body() {
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
    public void accept(NodeMessageDigester digester) {
        digester.accept(this);
    }
}
