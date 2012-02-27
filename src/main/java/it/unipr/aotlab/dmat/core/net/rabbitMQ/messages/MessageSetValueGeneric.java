package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsBytesWire;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

public class MessageSetValueGeneric extends MessageMatrixValues {
    public MatrixPieceTripletsBytesWire.MatrixPieceTripletsBytesBody body;

    public MessageSetValueGeneric(MatrixPieceTripletsBytesWire.MatrixPieceTripletsBytesBody body) {
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
        String m = super.toString() + " (" + body.getMatrixId() + ")";

        if (body.getValuesCount() > 0) {
            MatrixPieceTripletsBytesWire.Triplet t = body.getValues(0);
            m += " (" + t.getRow() + ", " + t.getCol() + ")";
        }

        return m;
    }

    @Override
    public String getMatrixId() {
        return body.getMatrixId();
    }

    @Override
    public Rectangle getArea() {
        return Rectangle.build(body.getPosition());
    }

    @Override
    public int getColRep() {
        return body.getPosition().getStartCol();
    }

    @Override
    public int getRowRep() {
        return body.getPosition().getStartRow();
    }
}
