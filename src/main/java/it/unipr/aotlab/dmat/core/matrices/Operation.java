package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody;
import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire.SemiRing;
import it.unipr.aotlab.dmat.core.net.MessageSender;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSendMatrixPiece;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

public abstract class Operation {
    public void exec() throws DMatError, IOException {
        precondition();

        neededChunks();

        fillinComputingNodes();

        nodeFitness();

        splitWork();

        sendMissingPiecesOrders();

        sendOperationsOrders();
    }

    // Check stuff like matrices size
    protected abstract void otherPreconditions() throws DMatError;

    // You have a chunk in the output matrix, prepare the list of
    // needed chunks to update it.
    protected abstract List<WorkZone>
        neededChunksToUpdateThisChunk(Chunk outputMatrixChunk);

    // You have the list of NodeWorkZonePair, where each working
    // node is associate to its work. Send the orders.
    protected abstract void sendOperationsOrders() throws IOException;

    public abstract int arity();

    protected void sendMissingPiecesOrders() throws IOException {
        OrderAwaitUpdateBody.Builder awaitUpdate = OrderAwaitUpdateBody.newBuilder();
        HashMap<PendingMissingPiecesMess, TreeSet<String>> pendingMessages
            = new HashMap<PendingMissingPiecesMess, TreeSet<String>>();

        // for each computing node
        for (NodeWorkZonePair nodeAndworkZone : workers) {
            Node computingNode = nodeAndworkZone.computingNode;
            String computingNodeId = computingNode.getNodeId();

            //for each output area it needs to be updated
            for (WorkZone workZone : nodeAndworkZone.workZones) {
                if ( ! computingNode.doesManage(workZone.outputChunk.chunkId)) {
                    awaitUpdate.setMatrix(workZone.outputChunk.chunkId);
                    awaitUpdate.setOutputPiece(workZone.outputArea.convertToProto());
                    
                    getMessageSender()
                        .sendMessage(null, computingNode);
                }

                //for each (sub)chunk needed to update the output area
                for (NeededPieceOfChunk pc : workZone.involvedChunks) {
                    if ( ! computingNode.doesManage(pc.chunk.chunkId))
                        updatePendingMessage(pendingMessages, pc, computingNodeId);
                }
            }
        }

        doActualSending(pendingMessages);
    }

    protected void precondition() throws DMatError {
        if (arity() != operands.size())
            throw new DMatError(this.getClass().getCanonicalName() + " needs "
                    + arity() + " operands.");

        SemiRing t = operands.get(0).semiring;
        for (Matrix m : operands) {
            if (t != m.semiring)
                throw new DMatError("All the operands must be of the same type!");

            t = m.semiring;
        }

        otherPreconditions();
    }

    public int outputMatrixIndex() {
        return 0;
    }

    public Matrix getOutputMatrix() {
        return operands.get(outputMatrixIndex());
    }

    // The user fails to set computing nodes, set the default.
    protected TreeSet<Node> getDefaultComputingNodes() {
        TreeSet<Node> workers = new TreeSet<Node>(new Node.NodeComparor());

        for (Matrix operand : operands)
            for (Chunk chunk : operand.getChunks())
                workers.add(chunk.getAssignedNode());

        return workers;
    }

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

                for (NeededPieceOfChunk wz : o1.involvedChunks)
                    diffValue -= wz.chunk.getAssignedNode().getNodeId()
                        .equals(nodeId) ? 1 : 0;

                for (NeededPieceOfChunk wz : o2.involvedChunks)
                    diffValue += wz.chunk.getAssignedNode().getNodeId()
                        .equals(nodeId) ? 1 : 0;

                return diffValue;
            }

            public int compareSizes(WorkZone o1, WorkZone o2) {
                int diffValue = 0;

                for (NeededPieceOfChunk wz : o1.involvedChunks)
                    diffValue -= wz.chunk.nofElements();

                for (NeededPieceOfChunk wz : o2.involvedChunks)
                    diffValue += wz.chunk.nofElements();

                return diffValue;
            }

            @Override
            public int compare(WorkZone o1, WorkZone o2) {
                int diffValue = compareNofLocalChunks(o1, o2);
                if (diffValue == 0) diffValue = compareSizes(o1, o2);

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
            worker.workZones = new LinkedList<WorkZone>(workZones);

            sortNodeFitness(worker.computingNode, worker.workZones);
            workers.add(worker);
        }
    }

    private int assign(int nodeNo) {
        int extraWork = workZones.size() % computingNodes.size();
        int nofWorkZonesForNode = workZones.size() / computingNodes.size();

        if (nodeNo < extraWork)
            ++nofWorkZonesForNode;

        return nofWorkZonesForNode;
    }

    private void splitWork() {
        int nodeNo = 0;

        for (NodeWorkZonePair nodeNWorkzone : workers) {
            takeWorkZones(nodeNWorkzone, assign(nodeNo));

            ++nodeNo;
        }
    }


    protected MessageSender getMessageSender() {
        return computingNodes.first().getMessageSender();
    }

    protected TreeSet<Node> computingNodes = null;
    protected List<NodeWorkZonePair> workers = new LinkedList<NodeWorkZonePair>();
    protected ArrayList<Matrix> operands = new ArrayList<Matrix>();

    public static class NeededPieceOfChunk {
        public Chunk chunk;
        public Rectangle piece;

        public NeededPieceOfChunk(Chunk chunk, Rectangle piece) {
            this.chunk = chunk;
            this.piece = piece;
        }

        @Override
        public String toString() {
            return super.toString()
                    + "Chunk: " + chunk
                    + "piece: " + piece;
        }
    }

    public static class WorkZone {
        //output matrix influenced area
        public Rectangle outputArea;
        public Chunk outputChunk;

        public List<NeededPieceOfChunk> involvedChunks;

        public boolean assignedToANode = false;

        public WorkZone(Chunk outputChunk,
                        Rectangle outputArea,
                        List<NeededPieceOfChunk> involvedChunks) {
            this.outputChunk = outputChunk;
            this.outputArea = outputArea;
            this.involvedChunks = involvedChunks;
        }

        public WorkZone(WorkZone wz) {
            this.outputArea = wz.outputArea;
            this.involvedChunks = wz.involvedChunks;
            this.assignedToANode = wz.assignedToANode;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append(super.toString());

            for (NeededPieceOfChunk c : involvedChunks) {
                sb.append("\nChunk: ");
                sb.append(c);
            }

            sb.append("\noutputArea: ");
            sb.append(outputArea);

            sb.append("\nAppend: ");
            sb.append(assignedToANode);

            return sb.toString();
        }
    }

    public static class NodeWorkZonePair {
        public Node computingNode;
        public List<WorkZone> workZones;

        @Override
        public String toString() {
            return computingNode.getNodeId() + " " + workZones;
        }
    }

    private List<WorkZone> workZones;

    private void fillinComputingNodes() {
        if (this.computingNodes == null) {
            this.computingNodes = getDefaultComputingNodes();
        }
    }

    private void takeWorkZones(NodeWorkZonePair nwzp, int nofWz) {
        Iterator<WorkZone> wzi = nwzp.workZones.iterator();

        while (wzi.hasNext()) {
            WorkZone wz = wzi.next();
            if (!wz.assignedToANode && nofWz > 0) {
                wz.assignedToANode = true;
                --nofWz;
            } else
                wzi.remove();
        }
    }

    private static class PendingMissingPiecesMess implements Comparable<PendingMissingPiecesMess> {
        public String ownerNodeId;
        public String matrixId;
        public Rectangle neededPiece;

        public PendingMissingPiecesMess() {}

        @Override
        public int compareTo(PendingMissingPiecesMess rhs) {
            int rv = matrixId.compareTo(rhs.matrixId);
            if (rv == 0) rv = ownerNodeId.compareTo(rhs.ownerNodeId);
            if (rv == 0) rv = neededPiece.startRow - rhs.neededPiece.startRow;
            if (rv == 0) rv = neededPiece.endRow   - rhs.neededPiece.endRow ;
            if (rv == 0) rv = neededPiece.startCol - rhs.neededPiece.startCol;
            if (rv == 0) rv = neededPiece.endRow   - rhs.neededPiece.endRow;

            return rv;
        }
    }

    private static void updatePendingMessage(HashMap<PendingMissingPiecesMess,
                                             TreeSet<String>> pendingMessages,
                                             NeededPieceOfChunk neededPiece,
                                             String destination) {
        PendingMissingPiecesMess message = new PendingMissingPiecesMess();
        TreeSet<String> destinationList;

        message.ownerNodeId = neededPiece.chunk.getAssignedNodeId();
        message.matrixId    = neededPiece.chunk.matrixId;
        message.neededPiece = neededPiece.chunk.matrixArea;

        if ((destinationList = pendingMessages.get(message)) != null) {
            destinationList.add(destination);
        } else {
            TreeSet<String> firstDestination = new TreeSet<String>();
            firstDestination.add(destination);
            pendingMessages.put(message, firstDestination);
        }
    }

    private void doActualSending(HashMap<PendingMissingPiecesMess,
                                 TreeSet<String>> pendingMessages) throws IOException {
        SendMatrixPieceBody.Builder messageBody = SendMatrixPieceBody.newBuilder();
        messageBody.setUpdate(false);

        for (Entry<PendingMissingPiecesMess, TreeSet<String>> task : pendingMessages.entrySet()) {
            PendingMissingPiecesMess message = task.getKey();
            TreeSet<String> destinations = task.getValue();

            messageBody.setMatrixId(message.matrixId)
                       .setNeededPiece(message.neededPiece.convertToProto());

            for (String destination : destinations)
                messageBody.addRecipient(destination);

            getMessageSender().sendMessage(new MessageSendMatrixPiece(messageBody.build()),
                                           message.ownerNodeId);
        }
    }
}
