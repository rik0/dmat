package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body;
import it.unipr.aotlab.dmat.core.generated.support.MatrixPieceTripletsInt32WireSupport;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.CompressedColsInt32;
import it.unipr.aotlab.dmat.core.matrixPiece.CompressedRowsInt32;
import it.unipr.aotlab.dmat.core.matrixPiece.Int32Triplet;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceTripletsInt32;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.protobuf.ByteString;

public class MessageMatrixPieceInt32 extends MessageMatrixValues {
    private MatrixPieceTripletsInt32Body realBody;
    public MatrixPieceTripletsInt32Body.Builder builder;

    MessageMatrixPieceInt32(MatrixPieceTripletsInt32Body body) {
        this.realBody = body;
    }

    public MessageMatrixPieceInt32(MatrixPieceTripletsInt32Body.Builder builder) {
        this.builder = builder;
    }

    public MatrixPieceTripletsInt32Body body() {
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
    public String getMatrixId() {
        return MatrixPieceTripletsInt32WireSupport.getMatrixId(body());
    }

    @Override
    public void dispatch(InNodeChunk<?> node) {
        node.accept(this);
    }

    @Override
    public Rectangle getPosition() {
        return Rectangle.build(body().getPosition());
    }

    @Override
    public int getColRep() {
        return body().getPosition().getStartCol();
    }

    @Override
    public int getRowRep() {
        return body().getPosition().getStartRow();
    }

    @Override
    public boolean getUpdate() {
        return body().getUpdate();
    }

    private class MessageMatrixIterator implements Iterator<Triplet> {
        int end;
        int current;

        public MessageMatrixIterator() {
            this.current = 0;
            this.end = body().getValuesCount();
        }

        @Override
        public boolean hasNext() {
            return current < end;
        }

        @Override
        public Int32Triplet next() {
            if (hasNext()) {
                MatrixPieceTripletsInt32Wire.Triplet t = body().getValues(current);
                ++current;

                return new Int32Triplet(t.getRow(), t.getCol(), t.getValue());
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Triplet> matrixPieceIterator() {
        return new MessageMatrixIterator();
    }

    @Override
    public String getNodeId() {
        return body().getNodeId();
    }

    @Override
    public MatrixPieces.Builder getAppropriatedBuilder() {
        return new MatrixPieceTripletsInt32.Builder();
    }

    @Override
    public boolean doesManage(int row, int col) {
        return getPosition().contains(row, col);
    }

    CompressedColsInt32 columnIteratorMaker = null;
    @Override
    public Iterator<Triplet> matrixColumnIterator(int col) {
        if (columnIteratorMaker == null) {
            System.err.println("XXX converting message in CompressedCols!");
            columnIteratorMaker = new CompressedColsInt32(body());
        }

        return columnIteratorMaker.getColIterator(col);
    }

    CompressedRowsInt32 rowIteratorMaker = null;
    @Override
    public Iterator<Triplet> matrixRowterator(int row) {
        if (rowIteratorMaker == null) {
            System.err.println("XXX converting message in CompressedRows!");
            rowIteratorMaker = new CompressedRowsInt32(body());
        }

        return rowIteratorMaker.getRowIterator(row);
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
