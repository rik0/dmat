package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;

import java.util.Iterator;

public class MatrixPieceTripletsInt32 implements MatrixPiece {
    static ChunkDescription.MatricesOnTheWire srtag = ChunkDescription.MatricesOnTheWire.INT32TRIPLETS;

    private int index = 0;
    private MatrixPieceTripletsInt32Wire.Body int32Triples;

    static {
        MatrixPieceTripletsInt32.Builder b = new Builder();
        MatrixPieces.defaultMatrixPieces.put(
                ChunkDescription.ElementType.INT32, b);
        MatrixPieces.defaultMatrixPieces.put(
                ChunkDescription.ElementType.UINT32, b);

        MatrixPieces.matrixPieces.put(srtag, b);
    }

    static class Builder implements MatrixPieces.Builder {
        @Override
        public MatrixPiece build(Object messageBody) {
            return new MatrixPieceTripletsInt32(
                    (MatrixPieceTripletsInt32Wire.Body) messageBody);
        }
    }

    public static class Int32Triplet {
        public int row;
        public int col;
        public int value;

        public Int32Triplet(int row, int col, int value) {
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
            MatrixPieceTripletsInt32Wire.Triplet t = int32Triples
                    .getValues(index);
            Int32Triplet next_ = new Int32Triplet(t.getRow(), t.getCol(),
                    t.getValue());
            ++index;

            return next_;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    MatrixPieceTripletsInt32(MatrixPieceTripletsInt32Wire.Body int32Triples) {
        this.int32Triples = int32Triples;
    }

    @Override
    public Int32TripletIterator matrixPieceIterator() {
        return new Int32TripletIterator();
    }
}
