package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceMarker;

import java.util.ArrayList;

public class NodeState {
    WorkingNode hostWorkingNode;
    ArrayList<InNodeChunk<?>> managedChunks = new ArrayList<InNodeChunk<?>>();
    ArrayList<MatrixPieceMarker> awaitingUpdate = new ArrayList<MatrixPieceMarker>();

    public boolean doesManage(String matrixId, String chunkId) {
        for (InNodeChunk<?> c : managedChunks) {
            if (c.chunk.getMatrixId().equals(matrixId)
                    && c.chunk.getChunkId().equals(chunkId))
                return true;
        }

        return false;
    }

    NodeState(WorkingNode hostWorkingNode) {
        this.hostWorkingNode = hostWorkingNode;
    }
}
