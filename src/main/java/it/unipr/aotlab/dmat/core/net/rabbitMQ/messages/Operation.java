package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import java.io.IOException;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

public abstract class Operation extends Message {
    public abstract void exec(NodeState nodeState) throws IOException ;
}
