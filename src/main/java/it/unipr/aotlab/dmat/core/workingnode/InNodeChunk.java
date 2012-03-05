package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;

public abstract class InNodeChunk<E> {
    WorkingNode hostNode = null;
    Chunk chunk = null;
    SemiRing semiring = null;
    ChunkAccessor accessor = null;
    MatrixPieces.Builder matrixPieceBuilder = null;

    InNodeChunk(Chunk chunk) {
    }

    public TypeWire.ElementType getType() {
        return chunk.getElementType();
    }

    public void accept(Message message) {
        throw new DMatInternalError("This node cannot accept "
                + message.getClass().getCanonicalName());
    }

    public MatrixPiece getMatrixPiece(Rectangle position, boolean isUpdate) {
        return accessor.getPiece(matrixPieceBuilder, position, isUpdate);
    }
}
