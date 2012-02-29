package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces.Builder;

public interface ChunkAccessor<E> {
    abstract public Chunk hostChunk();

    abstract public E get(int row, int col);

    abstract public E getDefault();

    abstract public void set(E value, int row, int col);

    abstract public MatrixPiece getPiece(Builder matrixPieceBuilder, Rectangle position, boolean isUpdate);
}
