package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageSetValueInt32 extends MessageSetValue {
    public MatrixPieceTripletsInt32Wire.Body body;

    public MessageSetValueInt32(MatrixPieceTripletsInt32Wire.Body body) {
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
        String m = super.toString() + " (" + body.getMatrixName() + ")";

        if (body.getValuesCount() > 0) {
            MatrixPieceTripletsInt32Wire.Triplet t = body.getValues(0);
            m += " (" + t.getRow() + ", " + t.getCol() + ") : " + t.getValue();
        }

        return m;
    }

    @Override
    public String getMatrixName() {
        return body.getMatrixName();
    }

    @Override
    public int getColRep() {
        if (body.getValuesCount() > 0) {
            return body.getValues(0).getCol();
        }

        return -1;
    }

    @Override
    public int getRowRep() {
        if (body.getValuesCount() > 0) {
            return body.getValues(0).getRow();
        }

        return -1;
    }
    
    @Override
    public void dispatch(InNodeChunk<?> node) {
        node.accept(this);
    }
}
