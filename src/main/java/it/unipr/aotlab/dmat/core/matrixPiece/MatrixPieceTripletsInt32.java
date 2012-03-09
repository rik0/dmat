package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixPieceInt32;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixValues;

import java.util.Collection;
import java.util.Iterator;

public class MatrixPieceTripletsInt32 implements MatrixPiece {
    static ChunkDescriptionWire.MatricesOnTheWire srtag = ChunkDescriptionWire.MatricesOnTheWire.INT32TRIPLETS;

    private int index = 0;
    private MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body int32Triples;

    static {
        MatrixPieceTripletsInt32.Builder b = new Builder();

        MatrixPieces.defaultMatrixPieces
            .put(TypeWire.ElementType.INT32, b);
        MatrixPieces.defaultMatrixPieces
            .put(TypeWire.ElementType.UINT32, b);

        MatrixPieces.matrixPieces.put(srtag, b);
    }

    public static class Builder implements MatrixPieces.Builder {
        @Override
        public MatrixPiece buildFromMessageBody(Object messageBody) {
            return new MatrixPieceTripletsInt32(
                    (MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body) messageBody);
        }

        @Override
        public MessageMatrixValues buildMessage(MatrixPiece matrixPieceUnt) {
            MatrixPieceTripletsInt32 matrixPiece = (MatrixPieceTripletsInt32)matrixPieceUnt;
            return new MessageMatrixPieceInt32(matrixPiece.int32Triples);
        }

        @Override
        public MatrixPiece buildFromChunk(ChunkAccessor format, Rectangle position, boolean isUpdate) {
            MatrixPieceTripletsInt32Body.Builder b = MatrixPieceTripletsInt32Body.newBuilder();
            MatrixPieceTripletsInt32Wire.Triplet.Builder trl = MatrixPieceTripletsInt32Wire.Triplet.newBuilder();

            b.setUpdate(isUpdate);
            b.setPosition(position.convertToProto());
            b.setChunkId(format.hostChunk().getChunkId());
            b.setNodeId(format.hostChunk().getAssignedNodeId());

            int intDefault = (Integer) format.getDefault();
            int v;

            b.setMatrixId(format.hostChunk().getMatrixId());
            for (int r = position.startRow; r < position.endRow; ++r)
                for (int c = position.startCol; c < position.endCol; ++c) {
                    if ((v = (Integer) format.get(r, c)) != intDefault)
                        b.addValues(trl.setRow(r).setCol(c).setValue(v).build());
                }

            return new MatrixPieceTripletsInt32(b.build());
        }

        @Override
        public MatrixPiece buildFromTriplets(String matrixId,
                String chunkId,
                String nodeId,
                Collection<Triplet> triplets,
                Rectangle position,
                boolean isUpdate) {

            MatrixPieceTripletsInt32Body.Builder b = MatrixPieceTripletsInt32Body.newBuilder();
            MatrixPieceTripletsInt32Wire.Triplet.Builder trl = MatrixPieceTripletsInt32Wire.Triplet.newBuilder();

            b.setUpdate(isUpdate);
            b.setPosition(position.convertToProto());

            b.setMatrixId(matrixId);
            b.setChunkId(chunkId);
            b.setNodeId(nodeId);

            for (Triplet t : triplets) {
                b.addValues(trl
                        .setRow(t.row())
                        .setCol(t.col())
                        .setValue((Integer) t.value()).build());
            }

            return new MatrixPieceTripletsInt32(b.build());
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

    @Override
    public String getChunkId() {
        return this.int32Triples.getChunkId();
    }

    @Override
    public String getNodeId() {
        return this.int32Triples.getNodeId();
    }
}
