package it.unipr.aotlab.dmat.core.workingnode;

import java.util.Iterator;

import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceTripletsBytes;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceTripletsInt32;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSetValueGeneric;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSetValueInt32;

public class InNodeChunkInt32 extends InNodeChunk<Integer> {
    InNodeChunkInt32(Chunk chunk) {
        super(chunk);
    }

    @Override
    public void accept(MessageSetValueInt32 message) {
        MatrixPieceTripletsInt32 triplets = new MatrixPieceTripletsInt32(message.body);
        Iterator<MatrixPieceTripletsInt32.Int32Triplet> tripletIterator = triplets
                .matrixPieceIterator();

        while (tripletIterator.hasNext()) {
            MatrixPieceTripletsInt32.Int32Triplet triplet = tripletIterator.next();
            accessor.set(triplet.value, triplet.row, triplet.col);
        }
    }

    @Override
    public void accept(MessageSetValueGeneric message) {
        MatrixPieceTripletsBytes triplets = new MatrixPieceTripletsBytes(message.body);
        Iterator<MatrixPieceTripletsBytes.BytesTriplet> tripletIterator = triplets
                .matrixPieceIterator();

        while (tripletIterator.hasNext()) {
            MatrixPieceTripletsBytes.BytesTriplet triplet = tripletIterator.next();
            int value = triplet.value.asReadOnlyByteBuffer().getInt();
            accessor.set(value, triplet.row, triplet.col);
        }
    }
}
