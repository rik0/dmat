package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import java.util.Iterator;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;

public abstract class MessageMatrixValues extends Message {
    public abstract String getMatrixId();
    
    public abstract String getChunkId();
    
    public abstract String getNodeId();

    public abstract Rectangle getArea();
    
    public abstract boolean doesManage(int row, int col);

    public abstract void dispatch(InNodeChunk<?> node);

    public abstract int getColRep();

    public abstract int getRowRep();
    
    public abstract boolean getUpdate();
    
    public abstract Iterator<Triplet> matrixPieceIterator();
    
    public abstract Iterator<Triplet> matrixColumnIterator(int col);
    
    public abstract Iterator<Triplet> matrixRowterator(int row);
    
    public abstract MatrixPieces.Builder getAppropriatedBuilder();
    
    public static class SimpleComparator implements java.util.Comparator<MessageMatrixValues> {
        @Override
        public int compare(MessageMatrixValues lhs, MessageMatrixValues rhs) {
            int rv = lhs.getMatrixId().compareTo(rhs.getMatrixId());
            if (rv == 0) rv = lhs.getChunkId().compareTo(rhs.getChunkId()); 
            if (rv == 0) rv = lhs.getArea().compare(rhs.getArea()); 
            
            return rv;
        }
    }
}
