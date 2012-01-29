package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsBytesWire;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageSetValueGeneric extends MessageMatrixValues {
    public MatrixPieceTripletsBytesWire.Body body;

    public MessageSetValueGeneric(MatrixPieceTripletsBytesWire.Body body) {
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
    public void dispatch(InNodeChunk<?> node) {
        node.accept(this);
    }

    @Override
    public String toString() {
        String m = super.toString() + " (" + body.getMatrixName() + ")";

        if (body.getValuesCount() > 0) {
            MatrixPieceTripletsBytesWire.Triplet t = body.getValues(0);
            m += " (" + t.getRow() + ", " + t.getCol() + ")";
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
}
