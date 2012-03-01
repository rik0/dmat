package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;

import java.util.Iterator;

public interface MatrixPiece {
    public String getMatrixId();
    public ChunkDescriptionWire.MatricesOnTheWire getTag();
    public Iterator<?> matrixPieceIterator();
}
