package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderMultiplyWire.OrderMultiplyBody;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;
import java.util.Collection;

public class MessageMultiply extends Operation {
    public OrderMultiplyBody.Builder builder;
    private OrderMultiplyBody realBody;

    MessageMultiply(OrderMultiplyBody body) {
        this.realBody = body;
    }

    public MessageMultiply(OrderMultiplyBody.Builder builder) {
        this.builder = builder;
    }

    public OrderMultiplyBody body() {
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
    public void exec(NodeState nodeState) throws IOException {
        nodeState.exec(this);
    }

    @Override
    public int nofMissingPieces() {
        return body().getMissingPiecesCount();
    }

    @Override
    public MatrixPieceOwnerBody missingPiece(int n) {
        return body().getMissingPieces(n);
    }

    @Override
    public void recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }
}
