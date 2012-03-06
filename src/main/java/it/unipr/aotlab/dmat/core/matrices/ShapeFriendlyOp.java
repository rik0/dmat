package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;

import java.util.LinkedList;
import java.util.List;

public abstract class ShapeFriendlyOp extends Operation {

    @Override
    protected void otherPreconditions() throws DMatError {
        Matrix firstOperand = operands.get(0);
        Matrix secondOperand = operands.get(1);

        if ( ! (firstOperand.getNofRows() == secondOperand.getNofRows()
                && secondOperand.getNofCols() == secondOperand.getNofCols()))
            throw new DMatError("The two operators must have the same size!");
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

    private static void updateWorkZones(LinkedList<WorkZone> workZones, Chunk firstOpChunk, Chunk secondOpChunk) {
        Rectangle intersection;

        if (null != (intersection = firstOpChunk.intersection(secondOpChunk))) {
            List<Chunk> involvedChunks = new LinkedList<Chunk>();
            involvedChunks.add(firstOpChunk);
            involvedChunks.add(secondOpChunk);
            WorkZone workzone = new WorkZone(intersection, involvedChunks);

            workZones.add(workzone);
        }
    }
}
