package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.messages.MessageCopyMatrix;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class CopyMatrix extends ShapeFriendlyOp {
    @Override
    protected void sendOrder(OrderBinaryOpBody.Builder order,
                             String computingNode)
            throws IOException {
        getNodeWorkGroup().sendOrderRaw(
                (new MessageCopyMatrix(order)).serialNo(serialNo),
                 computingNode);
    }

    @Override
    protected void updateWorkZones(LinkedList<WorkZone> workZones,
                                   Chunk firstOpChunk,
                                   NeededPieceOfChunk secondOpChunkNInter) {
        Rectangle intersection = secondOpChunkNInter.piece;
        Assertion.isTrue(intersection != null, "Null intersection in ShapeFriendlyOp!");
        List<NeededPieceOfChunk> involvedChunks = new LinkedList<NeededPieceOfChunk>();

        involvedChunks.add(secondOpChunkNInter);

        WorkZone workzone = new WorkZone(firstOpChunk,
                                         intersection,
                                         involvedChunks);

        workZones.add(workzone);
    }

    @Override
    protected void fillinComputingNodes() {
        //CopyMatrix ignores the eventual workers list done by the user.
        TreeSet<Node> workers = new TreeSet<Node>(new Node.NodeComparor());

        for (Chunk chunk : operands.get(0).getChunks())
            workers.add(chunk.getAssignedNode());

        this.computingNodes = workers;
    }

    @Override
    protected void sortNodeFitness(Node node, List<WorkZone> workZones) {
        class WorkZonesComparator implements Comparator<WorkZone> {
            String nodeId;

            public WorkZonesComparator(Node node) {
                this.nodeId = node.getNodeId();
            }

            @Override
            public int compare(WorkZone lhs, WorkZone rhs) {
                int diff;

                diff = rhs.outputChunk.getAssignedNodeId().equals(nodeId)
                        ? 1 : -1000;
                diff -= lhs.outputChunk.getAssignedNodeId().equals(nodeId)
                        ? 1 : -1000;

                return diff;

            }
        }
        Collections.sort(workZones, new WorkZonesComparator(node));
    }

    @Override
    protected void splitWork() {
        for (NodeWorkZonePair nwzp : tasks) {
            String nodeId = nwzp.computingNode.getNodeId();
            Iterator<WorkZone> wzi = nwzp.workZones.iterator();

            while (wzi.hasNext()) {
                WorkZone wz = wzi.next();
                if ( ! wz.assignedToANode && wz.outputChunk
                                               .getAssignedNodeId()
                                               .equals(nodeId)) {
                    wz.assignedToANode = true;
                } else {
                    wzi.remove();
                }
            }
        }
    }
}
