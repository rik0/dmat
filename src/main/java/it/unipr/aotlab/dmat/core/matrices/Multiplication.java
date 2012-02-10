package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.matrices.Operation.WorkZone;

import java.util.List;

public class Multiplication extends Operation {

    @Override
    public int arity() {
        return 3;
    }

    @Override
    protected void otherPreconditions() throws DMatError {
        Matrix outputMatrix = operands.get(0);

        Matrix firstOperand = operands.get(1);
        Matrix secondOperand = operands.get(2);

        if ( ! (firstOperand.getNofCols() == secondOperand.getNofRows()
                && outputMatrix.getNofRows() == firstOperand.getNofRows()
                && outputMatrix.getNofCols() == secondOperand.getNofCols())) {
            throw new DMatError("Invalid matrix sizes for multiplication.");
        }
    }

    @Override
    protected List<WorkZone> neededChunksToUpdateThisChunk(Chunk outputMatrixChunk) {
        Matrix firstOperand = operands.get(1);
        Matrix secondOperand = operands.get(2);

        List<Chunk> chunks = firstOperand
                                .involvedChunks(outputMatrixChunk.getStartRow(),
                                                outputMatrixChunk.getEndRow(),
                                                0,
                                                firstOperand.getNofCols());

        chunks.addAll(secondOperand
                        .involvedChunks(0,
                                        secondOperand.getNofRows(),
                                        outputMatrixChunk.getStartCol(),
                                        outputMatrixChunk.getEndCol()));

        return chunks;
    }

    @Override
    protected void sendOrdersToWorkers() {
        // TODO Auto-generated method stub

    }

}
