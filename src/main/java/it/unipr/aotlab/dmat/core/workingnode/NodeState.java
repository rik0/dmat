package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceMarker;

import java.util.ArrayList;
import java.util.Collection;

public class NodeState {
    WorkingNode hostWorkingNode;
    Collection<InNodeChunk<?>> managedChunks = new ArrayList<InNodeChunk<?>>();
    ArrayList<MatrixPieceMarker> awaitingUpdate = new ArrayList<MatrixPieceMarker>();

    NodeState(WorkingNode hostWorkingNode) {
        this.hostWorkingNode = hostWorkingNode;
    }
}
