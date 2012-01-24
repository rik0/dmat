package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSetValueGeneric;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSetValueInt32;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;

public abstract class InNodeChunk<E> {
    WorkingNode hostNode = null;
    Chunk chunk = null;
    SemiRing<E> semiring = null;
    ChunkAccessor<E> accessor = null;
    MatrixPieces.Builder matrixPiece = null;

    InNodeChunk(Chunk chunk) {
    }

    public ChunkDescription.ElementType getType() {
        return chunk.getElementType();
    }

    public void accept(Message message) {
        throw new DMatInternalError("This node cannot accept "
                + message.getClass().getCanonicalName());
    }

    public void accept(MessageSetValueInt32 message) {
        throw new DMatInternalError("This node cannot accept "
                + message.getClass().getCanonicalName());
    }

    public void accept(MessageSetValueGeneric message) {
        throw new DMatInternalError("This node cannot accept "
                + message.getClass().getCanonicalName());
    }
}
