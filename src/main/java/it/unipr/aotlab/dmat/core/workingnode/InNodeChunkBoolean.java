package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceTripletsBytes;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSetValueGeneric;

import java.util.Iterator;

public class InNodeChunkBoolean extends InNodeChunk<Boolean> {
    InNodeChunkBoolean(Chunk chunk) {
        super(chunk);
    }

    @Override
    public void accept(MessageSetValueGeneric message) {
        MatrixPieceTripletsBytes triplets = new MatrixPieceTripletsBytes(message.body);
        Iterator<MatrixPieceTripletsBytes.BytesTriplet> tripletIterator = triplets
                .matrixPieceIterator();

        while (tripletIterator.hasNext()) {
            MatrixPieceTripletsBytes.BytesTriplet triplet = tripletIterator.next();

            byte value = triplet.value.asReadOnlyByteBuffer().get();

            accessor.set(value != 0 ? true : false, triplet.row, triplet.col);
        }
    }
}
