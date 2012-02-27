package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.net.MessageSender;
import it.unipr.aotlab.dmat.core.net.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

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

    // Check stuff like matrices size
    protected abstract void otherPreconditions() throws DMatError;

    // You have a chunk in the output matrix, prepare the list of
    // needed chunks to update it.
    protected abstract List<WorkZone> neededChunksToUpdateThisChunk(
            Chunk outputMatrixChunk);

    // The user fails to set computing nodes, set the default.
    protected TreeSet<Node> getDefaultComputingNodes() {
        TreeSet<Node> workers = new TreeSet<Node>(new Node.NodeComparor());

        for (Matrix operand : operands)
            for (Chunk chunk : operand.getChunks())
                workers.add(chunk.getAssignedNode());

        return workers;
    }

    // You have the list of NodeWorkZonePair, where each working
    // node is associate to is work. Send the orders.
    protected abstract void sendOrdersToWorkers() throws IOException;

    protected void neededChunks() {
        List<WorkZone> workZones = new LinkedList<WorkZone>();

        for (Chunk c : getOutputMatrix().getChunks()) {
            List<WorkZone> newWorkZones = neededChunksToUpdateThisChunk(c);
            workZones.addAll(newWorkZones);
        }

        this.workZones = workZones;
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

    private void sortNodeFitness(Node node, List<WorkZone> workZones) {
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

    private void nodeFitness() {
        workers.clear();

        for (Node node : computingNodes) {
            NodeWorkZonePair worker = new NodeWorkZonePair();
            worker.computingNode = node;
            worker.workZone = new LinkedList<WorkZone>(workZones);

            sortNodeFitness(worker.computingNode, worker.workZone);
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

        for (NodeWorkZonePair nodeNWorkzone : workers) {
            takeWorkZones(nodeNWorkzone, assign(nodeNo));

            nodeNo += 1;
        }
    }

    public void exec() throws DMatError, IOException {
        precondition();

        neededChunks();

        fillinComputingNodes();

        nodeFitness();

        splitWork();

        sendOrdersToWorkers();
    }
    
    protected MessageSender getMessageSender() {
        return computingNodes.first().getMessageSender();
    }

    protected TreeSet<Node> computingNodes = null;
    protected List<NodeWorkZonePair> workers = new LinkedList<NodeWorkZonePair>();
    protected ArrayList<Matrix> operands = new ArrayList<Matrix>();

    public static class WorkZone {
        //output matrix influenced area
        public Rectangle outputArea;

        public List<Chunk> involvedChunks;

        public boolean assigned = false;

        public WorkZone(Rectangle outputArea, List<Chunk> involvedChunks) {
            this.outputArea = outputArea;
            this.involvedChunks = involvedChunks;
        }

        public WorkZone(WorkZone wz) {
            this.outputArea = wz.outputArea;
            this.involvedChunks = wz.involvedChunks;
            this.assigned = wz.assigned;
        }
    }

    public static class NodeWorkZonePair {
        public Node computingNode;
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
