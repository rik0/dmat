package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;

import java.util.Iterator;

public class MatrixPieceTripletsInt32 implements MatrixPiece {
    static ChunkDescriptionWire.MatricesOnTheWire srtag = ChunkDescriptionWire.MatricesOnTheWire.INT32TRIPLETS;

    private int index = 0;
    private MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body int32Triples;

    static {
        MatrixPieceTripletsInt32.Builder b = new Builder();

        MatrixPieces.defaultMatrixPieces.put(
                ChunkDescriptionWire.ElementType.INT32, b);
        MatrixPieces.defaultMatrixPieces.put(
                ChunkDescriptionWire.ElementType.UINT32, b);

        MatrixPieces.matrixPieces.put(srtag, b);
    }

    public static class Builder implements MatrixPieces.Builder {
        @Override
        public MatrixPiece buildFromMessageBody(Object messageBody) {
            return new MatrixPieceTripletsInt32(
                    (MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body) messageBody);
        }

        @Override
        public <E> MatrixPiece buildFromChunk(ChunkAccessor<E> format,
                int startRow, int startCol, int endRow, int endCol) {
            MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body.Builder b = MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body
                    .newBuilder();
            int intDefault = (Integer) format.getDefault();
            int v;
            b.setMatrixId(format.hostChunk().getMatrixId());
            for (int r = startRow; r < endRow; ++r)
                for (int c = startCol; c < endCol; ++c) {
                    if ((v = (Integer) format.get(r, c)) != intDefault)
                        b.addValues(MatrixPieceTripletsInt32Wire.Triplet
                                .newBuilder().setRow(r).setCol(c).setValue(v));
                }
            return new MatrixPieceTripletsInt32(b.build());
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

    public MatrixPieceTripletsInt32(
            MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body int32Triples) {
        this.int32Triples = int32Triples;
    }

    @Override
    public Int32TripletIterator matrixPieceIterator() {
        return new Int32TripletIterator();
    }

    @Override
    public ChunkDescriptionWire.MatricesOnTheWire getTag() {
        return srtag;
    }

    @Override
    public String getMatrixId() {
        return this.int32Triples.getMatrixId();
    }
}
