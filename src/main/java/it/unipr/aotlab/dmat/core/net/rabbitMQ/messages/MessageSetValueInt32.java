package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.generated.support.MatrixPieceTripletsInt32WireSupport;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageSetValueInt32 extends MessageMatrixValues {
    public MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body body;

    public MessageSetValueInt32(MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body body) {
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
    public String toString() {
        return super.toString() + " " + MatrixPieceTripletsInt32WireSupport.sToString(body);
    }

    @Override
    public String getMatrixId() {
        return MatrixPieceTripletsInt32WireSupport.getMatrixId(body);
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
