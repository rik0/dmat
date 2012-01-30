package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.formats.dense.DenseInt32;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription.MatricesOnTheWire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsBytesWire;

import java.util.Iterator;

import com.google.protobuf.ByteString;

public class MatrixPieceTripletsBytes implements MatrixPiece {
    static ChunkDescription.MatricesOnTheWire srtag = ChunkDescription.MatricesOnTheWire.BYTES;

    private int index = 0;
    private MatrixPieceTripletsBytesWire.Body int32Triples;

    static {
        MatrixPieceTripletsBytes.Builder b = new Builder();
        MatrixPieces.matrixPieces.put(srtag, b);
    }

    public static class Builder implements MatrixPieces.Builder {
        @Override
        public MatrixPiece buildFromMessageBody(Object messageBody) {
            return new MatrixPieceTripletsBytes(
                    (MatrixPieceTripletsBytesWire.Body) messageBody);
        }

        @Override
        public <E> MatrixPiece buildFromChunk(ChunkAccessor<E> format,
                int startRow, int startCol, int endRow, int endCol) {
            throw new DMatInternalError(this.getClass().getCanonicalName() + " for " + format + "still unimplemented");
        }
    }

    public static class BytesTriplet {
        public int row;
        public int col;
        public ByteString value;

        public BytesTriplet(int row, int col, ByteString value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }
    }

    private class BytesTripletIterator implements Iterator<BytesTriplet> {
        @Override
        public boolean hasNext() {
            return index < int32Triples.getValuesCount();
        }

        @Override
        public BytesTriplet next() {
            MatrixPieceTripletsBytesWire.Triplet t = int32Triples
                    .getValues(index);
            BytesTriplet next_ = new BytesTriplet(t.getRow(), t.getCol(),
                    t.getValue());
            ++index;

            return next_;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public MatrixPieceTripletsBytes(MatrixPieceTripletsBytesWire.Body int32Triples) {
        this.int32Triples = int32Triples;
    }

    @Override
    public BytesTripletIterator matrixPieceIterator() {
        return new BytesTripletIterator();
    }

    @Override
    public MatricesOnTheWire getTag() {
        return srtag;
    }
}
