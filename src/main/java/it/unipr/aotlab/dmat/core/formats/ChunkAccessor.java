package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.net.MatrixPiece;

public interface ChunkAccessor<E> {
    abstract public E get(int row, int col);

    abstract public void set(E value, int row, int col);

    abstract public MatrixPiece getPiece(int startRow, int endRow,
            int startCol, int endCol);

}