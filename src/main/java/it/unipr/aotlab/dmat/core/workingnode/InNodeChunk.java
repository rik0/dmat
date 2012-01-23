package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.formats.Formats;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;
import it.unipr.aotlab.dmat.core.semirings.SemiRings;

public class InNodeChunk<E> {
    Chunk chunk;
    SemiRing<E> semiring;
    ChunkAccessor<E> accessor;
    MatrixPiece matrixPiece;

    @SuppressWarnings("unchecked")
    InNodeChunk(Chunk chunk) {
        this.chunk = chunk;
        this.accessor = (ChunkAccessor<E>) Formats.build(chunk);

        if (chunk.getSemiring().equals(
                ChunkDescription.SemiRing.DEFAULTSEMIRING))
            this.semiring = (SemiRing<E>) SemiRings.defaultSemiring(chunk
                    .getElementType());
        else
            this.semiring = (SemiRing<E>) SemiRings.semiring(chunk
                    .getSemiring());
    }
}
