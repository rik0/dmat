package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import java.io.IOException;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.generated.OrderMultiplyWire.OrderMultiplyBody;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

public class MessageMultiply extends Operation {
    public OrderMultiplyBody body;

    public MessageMultiply(OrderMultiplyBody body) {
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

    @Override
    public void exec(NodeState nodeState) throws IOException {
        nodeState.exec(this);
    }

    @Override
    public int nofMissingPieces() {
        return body.getMissingPiecesCount();
    }

    @Override
    public MatrixPieceOwnerBody missingPiece(int n) {
        return body.getMissingPieces(n);
    }
}
