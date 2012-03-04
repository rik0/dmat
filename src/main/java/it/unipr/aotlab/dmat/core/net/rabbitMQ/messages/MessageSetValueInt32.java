package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import java.util.Iterator;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.generated.support.MatrixPieceTripletsInt32WireSupport;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
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
    public void accept(NodeMessageDigester digester) {
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
    public void dispatch(InNodeChunk<?> node) {
        node.accept(this);
    }

    @Override
    public Rectangle getArea() {
        return Rectangle.build(body.getPosition());
    }

    @Override
    public int getRowRep() {
        return body.getPosition().getStartRow();
    }

    @Override
    public int getColRep() {
        return body.getPosition().getStartCol();
    }

    @Override
    public boolean getUpdate() {
        return body.getUpdate();
    }

    @Override
    public String getChunkId() {
        return body.getChunkId();
    }

    @Override
    public Iterator<Triplet> matrixPieceIterator() {
        throw new DMatInternalError("Wrong call?");
    }
}
