package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.net.Node;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public abstract class Operation {
    public abstract int arity();

    public void precondition() throws DMatError {
        if (arity() != operands.size())
            throw new DMatError(this.getClass().getCanonicalName() + " needs "
                    + arity() + " operands.");

        otherPreconditions();
    }

    public int outputMatrixIndex() {
        return 0;
    }

    public Matrix getOutputMatrix() {
        return operands.get(outputMatrixIndex());
    }

    protected abstract void otherPreconditions() throws DMatError;

    //what chunks do you need to update the argument chunk of the output matrix?
    protected abstract List<Chunk> neededChunksToUpdateThisChunk(
            Chunk outputMatrixChunk);

    //what subset of the output chuck is computable with every set of chunks?
    protected abstract WorkZone markOutputZoneForChunk(List<Chunk> c);

    List<WorkZone> organizeWork(List<List<Chunk>> chunkSets) {
        List<WorkZone> workzones = new LinkedList<WorkZone>();

        for (List<Chunk> chunkset : chunkSets) {
            workzones.add(markOutputZoneForChunk(chunkset));
        }

        return workzones;

    }

    List<List<Chunk>> neededChunks() {
        List<List<Chunk>> involvedChunks = new LinkedList<List<Chunk>>();

        for (Chunk c : getOutputMatrix().getChunks()) {
            involvedChunks.add(neededChunksToUpdateThisChunk(c));
        }

        return involvedChunks;
    }

    public void setOperands(Collection<Matrix> operands) throws DMatError {
        this.operands.clear();
        this.operands.addAll(operands);
    }

    public void setComputingNodes(Collection<Node> computingNodes) {
        this.computingNodes.clear();
        this.computingNodes.addAll(computingNodes);
    }

    public void setComputingNodes(Node... computingNodes) {
        setComputingNodes(Arrays.asList(computingNodes));
    }

    public void setOperands(Matrix... operands) throws DMatError {
        setOperands(Arrays.asList(operands));
    }

    public void sortWorkZones(Node node, List<WorkZone> workZones) {
        class WorkZonesComparator implements Comparator<WorkZone> {
            Node node;

            public WorkZonesComparator(Node node) {
                this.node = node;
            }

            public int compareNofLocalChunks(WorkZone o1, WorkZone o2) {
                int diffValue = 0;

                for (Chunk wz : o1.involvedChunk)
                    diffValue -= wz.getAssignedNode().getNodeId()
                            .equals(node.getNodeId()) ? 1 : 0;

                for (Chunk wz : o2.involvedChunk)
                    diffValue += wz.getAssignedNode().getNodeId()
                            .equals(node.getNodeId()) ? 1 : 0;

                return diffValue;
            }

            public int compareSizes(WorkZone o1, WorkZone o2) {
                int diffValue = 0;

                for (Chunk wz : o1.involvedChunk)
                    diffValue -= wz.nofElements();

                for (Chunk wz : o2.involvedChunk)
                    diffValue += wz.nofElements();

                return diffValue;
            }

            @Override
            public int compare(WorkZone o1, WorkZone o2) {
                int diffValue = 0;

                diffValue = compareNofLocalChunks(o1, o2);
                if (diffValue != 0)
                    return diffValue;

                diffValue = compareSizes(o1, o2);
                if (diffValue != 0)
                    return diffValue;

                return diffValue;
            }
        }
        
        Collections.sort(workZones, new WorkZonesComparator(node));
    }

    Vector<Node> computingNodes = new Vector<Node>();
    Vector<Matrix> operands = new Vector<Matrix>();

    public static class WorkZone {
        //output matrix influenced area
        public int startRow;
        public int endRow;
        public int startCol;
        public int endCol;

        public List<Chunk> involvedChunk;
    }
}