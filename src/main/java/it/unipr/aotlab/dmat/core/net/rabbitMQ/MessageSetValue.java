package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;

public abstract class MessageSetValue extends Message {
    public abstract String getMatrixName();

    public abstract int getColRep();

    public abstract int getRowRep();

    public abstract void dispatch(InNodeChunk<?> node);
}
