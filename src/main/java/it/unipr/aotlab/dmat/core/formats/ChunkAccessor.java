package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;

import java.util.Iterator;

public interface ChunkAccessor {
    public InNodeChunk<?> hostChunk();

    public Object get(int row, int col);

    public Object getDefault();

    public void set(Object value, int row, int col);

    public void set(Triplet t);

    public void setPosition(Object value, Rectangle position);

    public void setPositionToZero(Rectangle position);

    public void resetToZero();

    public MatrixPiece getPiece(MatrixPieces.Builder matrixPieceBuilder, Rectangle position, boolean isUpdate);

    public Iterator<Triplet> matrixPieceIterator(Rectangle r);

    public Iterator<Triplet> matrixRowIterator(int rowNo);

    public Iterator<Triplet> matrixColumnIterator(int columnNo);
}
