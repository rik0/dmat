package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPiece;
import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceListWire.SendMatrixPiece;
import it.unipr.aotlab.dmat.core.net.Message;

import java.util.Collection;

import com.google.protobuf.ByteString;

public abstract class MessageBinaryOp extends Operation {
    protected OrderBinaryOpBody realBody = null;
    public OrderBinaryOpBody.Builder builder = null;

    MessageBinaryOp(OrderBinaryOpBody body) {
        this.realBody = body;
    }

    public MessageBinaryOp(OrderBinaryOpBody.Builder builder) {
        this.builder = builder;
    }

    public OrderBinaryOpBody body() {
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
    public Message recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
        return this;
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }

    @Override
    public int nofMissingPieces() {
        return body().getMissingPieces().getMatrixPieceCount();
    }

    @Override
    public MatrixPiece missingPiece(int n) {
        return body().getMissingPieces().getMatrixPiece(n);
    }

    @Override
    public int nofPiacesAwaitingUpdate() {
        return body().getAwaitingUpdates().getMatrixPieceCount();
    }

    @Override
    public MatrixPiece awaitingUpdate(int n) {
        return body().getAwaitingUpdates().getMatrixPiece(n);
    }

    @Override
    public int nofPiecesToBeSent() {
        return body().getPiecesToSend().getSendMatrixPieceCount();
    }

    @Override
    public SendMatrixPiece pieceToBeSent(int n) {
        return body().getPiecesToSend().getSendMatrixPiece(n);
    }
}
