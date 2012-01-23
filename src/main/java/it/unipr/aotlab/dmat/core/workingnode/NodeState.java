package it.unipr.aotlab.dmat.core.workingnode;

import java.util.Collection;
import java.util.Vector;

public class NodeState {
    WorkingNode hostWorkingNode;
    Collection<InNodeChunk<?>> managedChunks = new Vector<InNodeChunk<?>>();

    NodeState(WorkingNode hostWorkingNode) {
        this.hostWorkingNode = hostWorkingNode;
    }
}
