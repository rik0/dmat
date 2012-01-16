package it.unipr.aotlab.dmat.core;

import it.unipr.aotlab.dmat.core.matrices.Chunk;

import java.util.Collection;
import java.util.Vector;

public class NodeState {
    WorkingNode hostWorkingNode;
    Collection<Chunk> managedChunks = new Vector<Chunk>();

    NodeState(WorkingNode hostWorkingNode) {
        this.hostWorkingNode = hostWorkingNode;
    }
}
