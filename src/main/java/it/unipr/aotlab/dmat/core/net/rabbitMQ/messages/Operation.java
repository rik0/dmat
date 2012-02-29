package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

public abstract class Operation extends Message {
    public abstract void exec(NodeState nodeState);
}
