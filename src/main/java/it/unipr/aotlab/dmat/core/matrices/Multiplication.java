package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
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

        List<Chunk> firstOpChunks
            = firstOperand.involvedChunks(outputMatrixChunk.getStartRow(),
                                          outputMatrixChunk.getEndRow(),
                                          0,
                                          firstOperand.getNofCols());

        Collections.sort(firstOpChunks, new Comparator<Chunk>() {
            @Override
            public int compare(Chunk o1, Chunk o2) {
                int rv = o1.getStartRow() - o2.getStartRow();
                if (rv == 0)
                    rv = o1.getEndRow() - o2.getEndRow();

                return rv;
            }});

        List<Chunk> secondOpChunks
            = secondOperand.involvedChunks(0,
                                           secondOperand.getNofRows(),
                                           outputMatrixChunk.getStartCol(),
                                           outputMatrixChunk.getEndCol());

        Collections.sort(secondOpChunks, new Comparator<Chunk> () {
            @Override
            public int compare(Chunk o1, Chunk o2) {
                int rv = o1.getStartCol() - o2.getStartCol();
                if (rv == 0)
                    rv = o1.getEndCol() - o2.getEndCol();

                return rv;
            }});

        return makeWorkzones(outputMatrixChunk, firstOpChunks, secondOpChunks);
    }

    private List<WorkZone> makeWorkzones(Chunk outputMatrixChunk,
                                         List<Chunk> firstOpChunks,
                                         List<Chunk> secondOpChunks) {
        Iterator<Chunk> firstOpIt = firstOpChunks.iterator();
        Iterator<Chunk> secondOpIt = secondOpChunks.iterator();

        List<Chunk> currentRowGroup = new LinkedList<Chunk>();
        List<Chunk> currentColGroup = new LinkedList<Chunk>();

        return null;
    }

    @Override
    protected void sendOrdersToWorkers() {
        // TODO Auto-generated method stub
    }
}
