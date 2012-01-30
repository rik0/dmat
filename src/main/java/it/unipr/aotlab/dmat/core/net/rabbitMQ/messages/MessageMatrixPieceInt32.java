package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32WireSupport;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageMatrixPieceInt32 extends MessageMatrixValues {
    public MatrixPieceTripletsInt32Wire.Body body;

    public MessageMatrixPieceInt32(MatrixPieceTripletsInt32Wire.Body body) {
        this.body = body;
    }

    @Override
    public byte[] message() {
        return body.toByteArray();
    }

    @Override
    public void exec(NodeMessageDigester digester) {
        digester.accept(this);
    }

    @Override
    public String getMatrixId() {
        return MatrixPieceTripletsInt32WireSupport.getMatrixName(body);
    }

    @Override
    public int getColRep() {
        return MatrixPieceTripletsInt32WireSupport.getColRep(body);
    }

    @Override
    public int getRowRep() {
        return MatrixPieceTripletsInt32WireSupport.getRowRep(body);
    }

    @Override
    public void dispatch(InNodeChunk<?> node) {
        node.accept(this);
    }
}
