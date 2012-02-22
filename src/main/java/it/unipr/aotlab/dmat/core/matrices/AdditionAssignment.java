package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssign;
import it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody;
import it.unipr.aotlab.dmat.core.net.Node;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class AdditionAssignment extends Operation {
    @Override
    public int arity() {
        return 2;
    }

    @Override
    protected void otherPreconditions() throws DMatError {
        Matrix firstOperand = operands.get(0);
        Matrix secondOperand = operands.get(1);

        if ( ! (firstOperand.getNofRows() == secondOperand.getNofRows()
                && secondOperand.getNofCols() == secondOperand.getNofCols()))
            throw new DMatError("The two AdditionAssignment operators must"
                                + " have the same size!");
    }

    @Override
    protected List<WorkZone> neededChunksToUpdateThisChunk(Chunk outputMatrixChunk) {
        LinkedList<WorkZone> workZones = new LinkedList<WorkZone>();
        Matrix secondOperand = operands.get(1);

        for (Chunk chunk : secondOperand.involvedChunks(outputMatrixChunk.getArea())) {
            updateWorkZones(workZones, outputMatrixChunk, chunk);
        }

        return workZones;
    }

    private void updateWorkZones(LinkedList<WorkZone> workZones, Chunk firstOpChunk, Chunk secondOpChunk) {
        Rectangle intersection;

        if (null != (intersection = firstOpChunk.intersection(secondOpChunk))) {
            List<Chunk> involvedChunks = new LinkedList<Chunk>();
            involvedChunks.add(firstOpChunk);
            involvedChunks.add(secondOpChunk);
            WorkZone workzone = new WorkZone(intersection, involvedChunks);

            workZones.add(workzone);
        }
    }

    @Override
    protected void sendOrdersToWorkers() throws IOException {
        OrderAddAssign.Builder order = OrderAddAssign.newBuilder();
        MatrixPieceOwnerBody.Builder missindMatrices = MatrixPieceOwnerBody.newBuilder();
        RectangleBody.Builder areas = RectangleBody.newBuilder();
        
        TreeSet<String> involvedChunks = new TreeSet<String>();

        for (NodeWorkZonePair nodeAndworkZone : workers) {
            Node node = nodeAndworkZone.node;

            for (WorkZone wz : nodeAndworkZone.workZone) {
                for (Chunk c : wz.involvedChunks) {
                    involvedChunks.add(c.matrixId + "." + c.chunkId);
                }
            }

            node.sendMessage(null);
        }
    }
}
