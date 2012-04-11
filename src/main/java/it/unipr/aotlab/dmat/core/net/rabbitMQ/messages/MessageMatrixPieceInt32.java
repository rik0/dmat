package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body;
import it.unipr.aotlab.dmat.core.generated.support.MatrixPieceTripletsInt32WireSupport;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.Int32Triplet;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceTripletsInt32;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    public byte[] message() {
        return body().toByteArray();
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
    public Rectangle getArea() {
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

    @Override
    public String getChunkId() {
        return body().getChunkId();
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
        return getArea().contains(row, col);
    }

    private class MessageRowIterator implements Iterator<Triplet> {
        // CONSIDER: we extract the whole row and sort it...
        //           better way?
        private class colComparator implements java.util.Comparator<Triplet> {
            @Override
            public int compare(Triplet lhs, Triplet rhs) {
                return lhs.col() - rhs.col();
            }
        }
        ArrayList<Triplet> row = new ArrayList<Triplet>();
        Iterator<Triplet> iterator;

        public MessageRowIterator(int row) {
            for (int r = 0, e = body().getValuesCount(); r < e; ++r) {
                MatrixPieceTripletsInt32Wire.Triplet rawT = body().getValues(r);

                if (row == rawT.getRow())
                    this.row.add(new Int32Triplet(rawT.getRow(),
                                                  rawT.getCol(),
                                                  rawT.getValue()));
            }
            Collections.sort(this.row, new colComparator());

            iterator = this.row.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Triplet next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class MessageColIterator implements Iterator<Triplet> {
        private class rowComparator implements java.util.Comparator<Triplet> {
            @Override
            public int compare(Triplet lhs, Triplet rhs) {
                return lhs.row() - rhs.row();
            }
        }
        ArrayList<Triplet> col = new ArrayList<Triplet>();
        Iterator<Triplet> iterator;

        public MessageColIterator(int col) {
            for (int r = 0, e = body().getValuesCount(); r < e; ++r) {
                MatrixPieceTripletsInt32Wire.Triplet rawT = body().getValues(r);

                if (col == rawT.getCol())
                    this.col.add(new Int32Triplet(rawT.getRow(),
                                                  rawT.getCol(),
                                                  rawT.getValue()));
            }
            Collections.sort(this.col, new rowComparator());

            iterator = this.col.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Triplet next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Triplet> matrixColumnIterator(int col) {
        return new MessageColIterator(col);
    }

    @Override
    public Iterator<Triplet> matrixRowterator(int row) {
        return new MessageRowIterator(row);
    }

    @Override
    public void recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }
}
