package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;

public abstract class MessageMatrixValues extends Message {
    public abstract String getMatrixId();

    public abstract int getColRep();

    public abstract int getRowRep();

    public abstract void dispatch(InNodeChunk<?> node);
}
