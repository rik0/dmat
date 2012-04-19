package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.OrderBinaryOpWire.OrderBinaryOpBody;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageCopyMatrix;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
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

        WorkZone workzone = new WorkZone(firstOpChunk, intersection, involvedChunks);
        workZones.add(workzone);
    }

    @Override
    protected TreeSet<Node> getDefaultComputingNodes() {
        TreeSet<Node> workers = new TreeSet<Node>(new Node.NodeComparor());

        for (Chunk chunk : operands.get(0).getChunks())
            workers.add(chunk.getAssignedNode());

        return workers;
    }
}
