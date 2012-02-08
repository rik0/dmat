package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;

import java.util.List;

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
    protected List<Chunk> neededChunksToUpdateThisChunk(Chunk outputMatrixChunk) {
        Matrix firstOperand = operands.get(1);
        Matrix secondOperand = operands.get(2);

        List<Chunk> chunks = firstOperand
                                .involvedChunks(outputMatrixChunk.getStartRow(),
                                                outputMatrixChunk.getEndRow(),
                                                outputMatrixChunk.getStartCol(),
                                                outputMatrixChunk.getEndCol());

        chunks.addAll(secondOperand.involvedChunks(outputMatrixChunk.getStartRow(),
                                                outputMatrixChunk.getEndRow(),
                                                outputMatrixChunk.getStartCol(),
                                                outputMatrixChunk.getEndCol()));

        return chunks;
    }

    @Override
    protected WorkZone markOutputZoneForChunk(List<Chunk> c) {
        WorkZone wz = new WorkZone(c);
        Chunk outputChunk = c.get(0);

        wz.startRow = outputChunk.getStartRow();
        wz.endRow = outputChunk.getEndRow();
        wz.startCol = outputChunk.getStartCol();
        wz.endCol = outputChunk.getEndCol();

        return wz;
    }

    @Override
    protected void sendOrdersToWorkers() {
        // TODO Auto-generated method stub
    }
}
