package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.net.Node;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

public abstract class Operation {
    public abstract int arity();

    protected void precondition() throws DMatError {
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

    //check stuff like matrices size
    protected abstract void otherPreconditions() throws DMatError;

    //what chunks do you need to update the argument chunk of the output matrix?
    protected abstract List<Chunk> neededChunksToUpdateThisChunk(
            Chunk outputMatrixChunk);

    //what subset of the output chuck is computable with those set of chunks?
    protected abstract WorkZone markOutputZoneForChunk(List<Chunk> c);

    //The user fails to set computing nodes, set the default.
    protected TreeSet<Node> getDefaultComputingNodes() {
        TreeSet<Node> workers = new TreeSet<Node>(new Node.NodeComparor());
        
        for (Matrix operand : operands)
            for (Chunk chunk : operand.getChunks())
                workers.add(chunk.getAssignedNode());
        
        return workers;
    }

    protected abstract void sendOrdersToWorkers();

    protected void prepareWorkZones(List<List<Chunk>> chunkSets) {
        List<WorkZone> workZones = new LinkedList<WorkZone>();

        for (List<Chunk> chunkset : chunkSets)
            workZones.add(markOutputZoneForChunk(chunkset));

        this.workZones = workZones;
    }

    protected List<List<Chunk>> neededChunks() {
        List<List<Chunk>> unmarkedWorkZones = new LinkedList<List<Chunk>>();

        for (Chunk c : getOutputMatrix().getChunks()) {
            List<Chunk> unmarkedWorkZone = neededChunksToUpdateThisChunk(c);
            unmarkedWorkZone.add(0, c);
            unmarkedWorkZones.add(unmarkedWorkZone);
        }

        return unmarkedWorkZones;
    }

    public void setOperands(Collection<Matrix> operands) throws DMatError {
        this.operands.clear();
        this.operands.addAll(operands);
    }

    public void setComputingNodes(Collection<Node> computingNodes) {
        this.computingNodes = new TreeSet<Node>();
        this.computingNodes.addAll(computingNodes);
    }

    public void setComputingNodes(Node... computingNodes) {
        setComputingNodes(Arrays.asList(computingNodes));
    }

    public void setOperands(Matrix... operands) throws DMatError {
        setOperands(Arrays.asList(operands));
    }

    private void sortWorkZones(Node node, List<WorkZone> workZones) {
        class WorkZonesComparator implements Comparator<WorkZone> {
            String nodeId;

            public WorkZonesComparator(Node node) {
                this.nodeId = node.getNodeId();
            }

            public int compareNofLocalChunks(WorkZone o1, WorkZone o2) {
                int diffValue = 0;

                for (Chunk wz : o1.involvedChunks)
                    diffValue -= wz.getAssignedNode().getNodeId()
                    .equals(nodeId) ? 1 : 0;

                for (Chunk wz : o2.involvedChunks)
                    diffValue += wz.getAssignedNode().getNodeId()
                    .equals(nodeId) ? 1 : 0;

                return diffValue;
            }

            public int compareSizes(WorkZone o1, WorkZone o2) {
                int diffValue = 0;

                for (Chunk wz : o1.involvedChunks)
                    diffValue -= wz.nofElements();

                for (Chunk wz : o2.involvedChunks)
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
                return diffValue;
            }
        }

        Collections.sort(workZones, new WorkZonesComparator(node));
    }

    private void associateNodesToWorkZones() {
        workers.clear();

        for (Node node : computingNodes) {
            NodeWorkZonePair worker = new NodeWorkZonePair();
            worker.node = node;
            worker.workZone = new LinkedList<WorkZone>(workZones);
            sortWorkZones(worker.node, worker.workZone);
            workers.add(worker);
        }
    }

    private int assign(int nodeNo) {
        int extraWorks = workZones.size() % computingNodes.size();
        int nofWorkZonesForNode = workZones.size() / computingNodes.size();

        if (nodeNo < extraWorks)
            nofWorkZonesForNode += 1;

        return nofWorkZonesForNode;
    }

    private void splitWork() {
        int nodeNo = 0;

        for (NodeWorkZonePair node : workers) {
            takeWorkZones(node, assign(nodeNo));

            nodeNo += 1;
        }
    }

    public void exec() throws DMatError {
        precondition();

        prepareWorkZones(neededChunks());

        associateNodesToWorkZones();

        fillinComputingNodes();

        splitWork();

        sendOrdersToWorkers();
    }

    protected TreeSet<Node> computingNodes = null;
    protected List<NodeWorkZonePair> workers = new LinkedList<NodeWorkZonePair>();
    protected Vector<Matrix> operands = new Vector<Matrix>();

    public static class WorkZone {
        //output matrix influenced area
        public int startRow;
        public int endRow;
        public int startCol;
        public int endCol;
        public List<Chunk> involvedChunks;
        public boolean assigned = false;

        public WorkZone(List<Chunk> involvedChunks) {
            this.involvedChunks = involvedChunks;
        }

        public WorkZone(WorkZone wz) {
            this.startRow = wz.startRow;
            this.endRow = wz.endRow;
            this.startCol = wz.startCol;
            this.endCol = wz.endCol;
            this.involvedChunks = wz.involvedChunks;
            this.assigned = wz.assigned;
        }
    }

    public static class NodeWorkZonePair {
        public Node node;
        public List<WorkZone> workZone;
    }


    private List<WorkZone> workZones;

    private void fillinComputingNodes() {
        if (this.computingNodes == null) {
            this.computingNodes = getDefaultComputingNodes();
        }
    }

    private void takeWorkZones(NodeWorkZonePair nwzp, int nofWz) {
        Iterator<WorkZone> wzi = nwzp.workZone.iterator();

        while (wzi.hasNext()) {
            WorkZone wz = wzi.next();
            if (!wz.assigned && nofWz > 0) {
                wz.assigned = true;
                --nofWz;
            } else
                wzi.remove();
        }
    }
}
