package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;

import java.util.Iterator;

public interface MatrixPiece {
    public String getMatrixId();
    public Rectangle getPosition();
    public String getNodeId();

    public ChunkDescriptionWire.MatricesOnTheWire getTag();
    public Iterator<?> matrixPieceIterator();
}
