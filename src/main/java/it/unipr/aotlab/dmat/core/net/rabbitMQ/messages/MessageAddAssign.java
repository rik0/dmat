package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;
import java.util.Collection;

import com.google.protobuf.ByteString;

public class MessageAddAssign extends Operation {
    private OrderAddAssignBody realBody = null;
    public OrderAddAssignBody.Builder builder = null;

    MessageAddAssign(OrderAddAssignBody body) {
        this.realBody = body;
    }

    public MessageAddAssign(OrderAddAssignBody.Builder builder) {
        this.builder = builder;
    }

    public OrderAddAssignBody body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }

        return realBody;
    }

    @Override
    public ByteString message() {
        return body().toByteString();
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
    public Message recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
        return this;
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }
}
