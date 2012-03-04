package it.unipr.aotlab.dmat.core.formats;

import java.util.Iterator;

import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces.Builder;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;

public interface ChunkAccessor {
    public Chunk hostChunk();

    public Object get(int row, int col);

    public Object getDefault();

    public void set(Object value, int row, int col);

    public MatrixPiece getPiece(Builder matrixPieceBuilder, Rectangle position, boolean isUpdate);
    
    public Iterator<Triplet> matrixPieceIterator(Rectangle r);
}
