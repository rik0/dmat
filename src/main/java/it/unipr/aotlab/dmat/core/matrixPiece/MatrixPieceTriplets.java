package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32;

import java.util.Iterator;

public class MatrixPieceTriplets implements MatrixPiece {
    private int index = 0;
    private MatrixPieceTripletsInt32.Body int32Triples;

    public static class Int32Triplet {
        public int row;
        public int col;
        public int value;

        public Int32Triplet(int row, int col, int value) {
            super();
            this.row = row;
            this.col = col;
            this.value = value;
        }
    }

    private class Int32TripletIterator implements Iterator<Int32Triplet> {
        @Override
        public boolean hasNext() {
            return index < int32Triples.getValuesCount();
        }

        @Override
        public Int32Triplet next() {
            MatrixPieceTripletsInt32.Triplet t = int32Triples.getValues(index);
            Int32Triplet next_ = new Int32Triplet(t.getRow(), t.getCol(), t.getValue());
            ++index;

            return next_;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public MatrixPieceTriplets(MatrixPieceTripletsInt32.Body int32Triples) {
        this.int32Triples = int32Triples;
        this.int32Triples.getValues(index);
    }

    @Override
    public Int32TripletIterator matrixPieceIterator() {
        return new Int32TripletIterator();
    }
}
