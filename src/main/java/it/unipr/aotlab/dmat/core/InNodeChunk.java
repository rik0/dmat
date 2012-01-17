package it.unipr.aotlab.dmat.core;

import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.formats.Formats;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;
import it.unipr.aotlab.dmat.core.semirings.SemiRings;

public class InNodeChunk<E> {
    Chunk chunk;
    SemiRing<E> semiring;
    ChunkAccessor<E> accessor;

    @SuppressWarnings("unchecked")
    public InNodeChunk(Chunk chunk) {
        this.chunk = chunk;
        this.accessor = (ChunkAccessor<E>) Formats.setFormat(chunk.getElementType(), chunk.getFormat(), chunk);
        this.semiring = (SemiRing<E>) SemiRings.semiring(chunk.getSemiring());
    }
}
