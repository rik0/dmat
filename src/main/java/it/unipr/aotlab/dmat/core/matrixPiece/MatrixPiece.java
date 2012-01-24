package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;

import java.util.Iterator;

public interface MatrixPiece {
    public ChunkDescription.MatricesOnTheWire getTag();
    public Iterator<?> matrixPieceIterator();
}
