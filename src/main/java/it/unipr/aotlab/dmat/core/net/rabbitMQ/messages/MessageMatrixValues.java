package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;

public abstract class MessageMatrixValues extends Message {
    public abstract String getMatrixId();
    
    public abstract String getChunkId();

    public abstract Rectangle getArea();

    public abstract void dispatch(InNodeChunk<?> node);

    public abstract int getColRep();

    public abstract int getRowRep();
    
    public abstract boolean getUpdate();
}
