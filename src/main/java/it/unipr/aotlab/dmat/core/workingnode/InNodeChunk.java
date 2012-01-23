package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;

public class InNodeChunk<E> {
    WorkingNode hostNode;
    Chunk chunk = null;
    SemiRing<E> semiring = null;
    ChunkAccessor<E> accessor = null;
    MatrixPieces.Builder matrixPiece = null;

    InNodeChunk(Chunk chunk) {
    }
}
