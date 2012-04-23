package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPiece;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPieceListBody;
import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceListWire.SendMatrixPiece;
import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceListWire.SendMatrixPieceListBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire.SemiRing;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageDummyOrder;
import it.unipr.aotlab.dmat.core.registers.NodeWorkGroupBoth;
import it.unipr.aotlab.dmat.core.util.Assertion;

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

        partitionNodeList();

        getOperationSerialNo();

        nodeFitness();

        splitWork();

        prepareMissingPiecesOrders();

        sendOrdersToComputingNodes();

        sendOrdersToStorageNodes();

        sendOrdersToUnusedNodes();

        awaitAnswer();
    }

    // Check stuff like matrices size
    protected abstract void otherPreconditions() throws DMatError;

    // You have a chunk in the output matrix, prepare the list of
    // needed chunks to update it.
    protected abstract List<WorkZone>
        neededChunksToUpdateThisChunk(Chunk outputMatrixChunk);

    //If the operation is ``non-void'' implements here the facts that
    //the master awaits the answer.
    protected void awaitAnswer() {}

    // You have the list of NodeWorkZonePair, where each working
    // node is associated with its work. Send the orders.
    protected abstract void sendOrdersToComputingNodes() throws IOException;

    protected abstract void sendOrdersToStorageNodes() throws IOException;

    protected void sendOrdersToUnusedNodes() throws IOException {
        for (String nodeId : unusedNodes) {
            getNodeWorkGroup().sendOrderRaw((new MessageDummyOrder())
                    .serialNo(serialNo), nodeId);
        }
    }

    public abstract int arity();

    protected void prepareMissingPiecesOrders() throws IOException {
        MatrixPiece.Builder awaitUpdate = MatrixPiece.newBuilder();
        HashMap<PendingMissingPiecesMess, TreeSet<String>> pendingMessages
            = new HashMap<PendingMissingPiecesMess, TreeSet<String>>();

        // for each computing node
        for (NodeWorkZonePair nodeAndworkZone : tasks) {
            Node computingNode = nodeAndworkZone.computingNode;
            String computingNodeId = computingNode.getNodeId();

            //for each output area it needs to be updated
            for (WorkZone workZone : nodeAndworkZone.workZones) {
                //update ``awaiting update'' if needed.
                if ( ! computingNode.doesManage(workZone.outputChunk.chunkId)) {
                    awaitUpdate.setMatrixId(workZone.outputChunk.matrixId);
                    awaitUpdate.setPosition(workZone.outputArea
                                                    .convertToProto());

                    String owner = workZone.outputChunk
                            .getAssignedNode()
                            .getNodeId();

                    updatePieces2await(owner, awaitUpdate.build());
                }

                //for each (sub)chunk needed to update this output area
                for (NeededPieceOfChunk pc : workZone.involvedChunks) {
                    if ( ! computingNode.doesManage(pc.chunk.chunkId)) {
                        updatePendingMessage(pendingMessages,
                                             pc,
                                             computingNodeId);
                    }
                }
            }
        }

        updateMatrixPieces2beSent(pendingMessages);
    }

    private void updatePieces2await(String owner, MatrixPiece matrixPiece) {
        MatrixPieceListBody.Builder pieces = pieces2await.get(owner);
        if (pieces == null) {
            pieces = MatrixPieceListBody.newBuilder();
            pieces2await.put(owner, pieces);
        }

        pieces.addMatrixPiece(matrixPiece);
    }

    protected void precondition() throws DMatError {
        if (arity() != operands.size())
            throw new DMatError(this.getClass().getCanonicalName() + " needs "
                    + arity() + " operands.");

        SemiRing t = operands.get(0).getSemiRing();
        for (Matrix m : operands) {
            if (t != m.getSemiRing())
                throw new DMatError("All the operands must be of the same type!");

            t = m.getSemiRing();
        }

        otherPreconditions();
    }

    public int outputMatrixIndex() {
        return 0;
    }

    public Matrix getOutputMatrix() {
        return operands.get(outputMatrixIndex());
    }

    // The user failed to set computing nodes, set the default.
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

    protected void sortNodeFitness(Node node, List<WorkZone> workZones) {
        class WorkZonesComparator implements Comparator<WorkZone> {
            Node node;
            String nodeId;

            public WorkZonesComparator(Node node) {
                this.node = node;
                this.nodeId = node.getNodeId();
            }

            public int compareOutputChunkPresence(WorkZone lhs, WorkZone rhs) {
                int rhsv = node.doesManage(rhs.outputChunk.chunkId) ? 1 : 0;
                int lhsv = node.doesManage(lhs.outputChunk.chunkId) ? 1 : 0;

                return lhsv - rhsv;
            }

            public int compareNofLocalChunks(WorkZone lhs, WorkZone rhs) {
                int diffValue = 0;

                for (NeededPieceOfChunk wz : lhs.involvedChunks)
                    diffValue -= wz.chunk.getAssignedNode().getNodeId()
                        .equals(nodeId) ? 1 : 0;

                for (NeededPieceOfChunk wz : rhs.involvedChunks)
                    diffValue += wz.chunk.getAssignedNode().getNodeId()
                        .equals(nodeId) ? 1 : 0;

                return diffValue;
            }

            public int compareSizes(WorkZone lhs, WorkZone rhs) {
                int diffValue = 0;

                for (NeededPieceOfChunk wz : lhs.involvedChunks)
                    diffValue -= wz.chunk.nofElements();

                for (NeededPieceOfChunk wz : rhs.involvedChunks)
                    diffValue += wz.chunk.nofElements();

                return diffValue;
            }

            @Override
            public int compare(WorkZone lhs, WorkZone rhs) {
                int diffValue = compareNofLocalChunks(lhs, rhs);
                if (diffValue == 0) diffValue = compareOutputChunkPresence(lhs, rhs);
                if (diffValue == 0) diffValue = compareSizes(lhs, rhs);

                return diffValue;
            }
        }

        Collections.sort(workZones, new WorkZonesComparator(node));
    }

    private void nodeFitness() {
        tasks.clear();

        for (Node node : computingNodes) {
            NodeWorkZonePair worker = new NodeWorkZonePair();
            worker.computingNode = node;
            worker.workZones = new LinkedList<WorkZone>(workZones);

            sortNodeFitness(worker.computingNode, worker.workZones);
            tasks.add(worker);
        }
    }

    private int assign(int nodeNo) {
        int extraWork = workZones.size() % computingNodes.size();
        int nofWorkZonesForNode = workZones.size() / computingNodes.size();

        if (nodeNo < extraWork)
            ++nofWorkZonesForNode;

        return nofWorkZonesForNode;
    }

    protected void splitWork() {
        int nodeNo = 0;

        for (NodeWorkZonePair nodeNWorkzone : tasks) {
            takeWorkZones(nodeNWorkzone, assign(nodeNo));

            ++nodeNo;
        }
    }


    protected NodeWorkGroupBoth getNodeWorkGroup() {
        return (NodeWorkGroupBoth) computingNodes.first().getWorkGroup();
    }

    protected TreeSet<Node> computingNodes = null;
    protected List<NodeWorkZonePair> tasks = new LinkedList<NodeWorkZonePair>();
    protected ArrayList<Matrix> operands = new ArrayList<Matrix>();

    protected HashMap<String, SendMatrixPieceListBody.Builder> pieces2beSent
        = new HashMap<String, SendMatrixPieceListBody.Builder>();
    protected HashMap<String, MatrixPieceListBody.Builder> pieces2await
        = new HashMap<String, MatrixPieceListBody.Builder>();
    TreeSet<String> storageNodes = new TreeSet<String>();
    TreeSet<String> unusedNodes = new TreeSet<String>();

    public static class NeededPieceOfChunk {
        public Chunk chunk;
        public Rectangle piece;

        public NeededPieceOfChunk(Chunk chunk, Rectangle piece) {
            this.chunk = chunk;
            this.piece = piece;
        }

        @Override
        public String toString() {
            return  this.getClass().getSimpleName() + " - Chunk: " + chunk
                    + ", Piece: " + piece;
        }
    }

    public static class WorkZone {
        //output matrix influenced area
        public Rectangle outputArea;
        public Chunk outputChunk;

        public List<NeededPieceOfChunk> involvedChunks;

        boolean assignedToANode = false;

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
    protected int serialNo;

    protected void fillinComputingNodes() {
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
        public String toString() {
            return " node:" + ownerNodeId + " matrix:" + matrixId + " piece:" + neededPiece;
        }

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

        message.ownerNodeId = neededPiece.chunk.getAssignedNodeId();
        message.matrixId    = neededPiece.chunk.matrixId;
        message.neededPiece = neededPiece.piece;

        TreeSet<String> destinationList = pendingMessages.get(message);
        if (destinationList == null) {
            destinationList = new TreeSet<String>();
            pendingMessages.put(message, destinationList);
        }

        destinationList.add(destination);
    }

    private void updateMatrixPieces2beSent(HashMap<PendingMissingPiecesMess,
                                 TreeSet<String>> pendingMessages) throws IOException {
        for (Entry<PendingMissingPiecesMess, TreeSet<String>> task : pendingMessages.entrySet()) {
            SendMatrixPiece.Builder singlePiece = SendMatrixPiece.newBuilder();
            singlePiece.setUpdate(false);

            PendingMissingPiecesMess message = task.getKey();
            TreeSet<String> destinations = task.getValue();

            singlePiece.setMatrixId(message.matrixId)
                       .setNeededPiece(message.neededPiece.convertToProto());

            for (String destination : destinations)
                singlePiece.addRecipient(destination);

            updateMatrixPieces2beSent(message, singlePiece.build());
        }

    }

    private void updateMatrixPieces2beSent(PendingMissingPiecesMess message,
                                           SendMatrixPiece messageBody) {
        SendMatrixPieceListBody.Builder owner
                = pieces2beSent.get(message.ownerNodeId);
        if (owner == null) {
            owner = SendMatrixPieceListBody.newBuilder();
            pieces2beSent.put(message.ownerNodeId, owner);
        }

        owner.addSendMatrixPiece(messageBody);
    }

    private void getOperationSerialNo() {
        serialNo = getNodeWorkGroup().getNextOrderId();
    }

    protected SendMatrixPieceListBody pieces2BeSentProto(String nodeId) {
        SendMatrixPieceListBody.Builder list = this.pieces2beSent.get(nodeId);
        if (list != null)
            return list.build();

        return SendMatrixPieceListBody.getDefaultInstance();
    }

    protected MatrixPieceListBody awaitingUpdateProto(String nodeId) {
        MatrixPieceListBody.Builder list = this.pieces2await.get(nodeId);
        if (list != null)
            return list.build();

        return MatrixPieceListBody.getDefaultInstance();
    }

    protected void updateMissingPieces(MatrixPieceListBody.Builder missingPieces,
                                       WorkZone wz,
                                       Node computingNode) {
        MatrixPiece.Builder missingPiece = MatrixPiece.newBuilder();
        for (NeededPieceOfChunk c : wz.involvedChunks) {
            if ( ! computingNode.doesManage(c.chunk.chunkId)) {
                missingPiece.setMatrixId(c.chunk.getMatrixId())
                .setPosition(c.piece.convertToProto());
                missingPieces.addMatrixPiece(missingPiece.build());
            }
        }
    }

    private void partitionNodeList() {
        unusedNodes.addAll(getNodeWorkGroup().nodesId());

        for (Node computingNode : computingNodes) {
            boolean present = unusedNodes.remove(computingNode.getNodeId());
            Assertion.isTrue(present,
                             "A computing node was not in the workgroup!");
        }

        for (Matrix matrix : this.operands) {
            for (Chunk chunk : matrix.getChunks()) {
                String nodeId = chunk.assignedTo.getNodeId();
                if (unusedNodes.remove(nodeId))
                    storageNodes.add(nodeId);
            }
        }
    }
}
