package it.unipr.aotlab.dmat.core.formats;

import java.util.Iterator;

import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces.Builder;

public interface ChunkAccessor<E> {
    public Chunk hostChunk();

    public E get(int row, int col);

    public E getDefault();

    public void set(E value, int row, int col);

    public MatrixPiece getPiece(Builder matrixPieceBuilder, Rectangle position, boolean isUpdate);
    
    public Iterator<?> matrixPieceIterator();
}
