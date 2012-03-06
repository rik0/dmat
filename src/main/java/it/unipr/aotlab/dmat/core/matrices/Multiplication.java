package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    protected ArrayList<WorkZone> neededChunksToUpdateThisChunk(Chunk outputMatrixChunk) {
        Matrix firstOperand = operands.get(1);
        Matrix secondOperand = operands.get(2);

        ArrayList<Chunk> firstOpChunks
            = firstOperand.involvedChunksAllCols(outputMatrixChunk.getStartRow(),
                                                 outputMatrixChunk.getEndRow());

        Collections.sort(firstOpChunks, new Chunk.RowsComparator());

        ArrayList<Chunk> secondOpChunks
            = secondOperand.involvedChunksAllRows(outputMatrixChunk.getStartCol(),
                                                  outputMatrixChunk.getEndCol());

        Collections.sort(secondOpChunks, new Chunk.ColumnsComparator());

        return makeWorkzones(outputMatrixChunk, firstOpChunks, secondOpChunks);
    }

    private ArrayList<WorkZone> makeWorkzones(Chunk outputMatrixChunk,
                                              ArrayList<Chunk> firstOpChunks,
                                              ArrayList<Chunk> secondOpChunks) {
        /* Creates workzones using first operand row division and
         * second operand columns division. */
        Matrix firstOperand = operands.get(1);
        Matrix secondOperand = operands.get(2);

        ArrayList<WorkZone> workZones = new ArrayList<WorkZone>();

        int rowGroupIndex = 0;
        int colGroupIndex = 0;

        do {
            rowGroupIndex = 0;
            Chunk secondOpChunk = secondOpChunks.get(colGroupIndex);
            ArrayList<Chunk> secondOpNeededChunks = secondOperand
                    .involvedChunksAllRows(secondOpChunk.getStartCol(),
                                           secondOpChunk.getEndCol());

            do {
                Chunk firstOpChunk = firstOpChunks.get(rowGroupIndex);
                ArrayList<Chunk> neededChunks = new ArrayList<Chunk>();

                neededChunks.add(outputMatrixChunk);
                neededChunks.addAll(firstOperand
                        .involvedChunksAllCols(firstOpChunk.getStartRow(),
                                               firstOpChunk.getEndRow()));
                neededChunks.addAll(secondOpNeededChunks);

                Rectangle outputArea = getOutputArea(outputMatrixChunk, firstOpChunk, secondOpChunk);

                workZones.add(new WorkZone(outputArea, neededChunks));
            } while (-1 != (rowGroupIndex = getNextRowGroup(rowGroupIndex, firstOpChunks)));
        } while (-1 != (colGroupIndex = getNextColGroup(colGroupIndex, secondOpChunks)));

        return workZones;
    }
    
    private static Rectangle getOutputArea(Chunk output, Chunk first, Chunk second) {
        int startRow = Math.max(output.getStartRow(), first.getStartRow());
        int endRow   = Math.min(output.getEndRow(), first.getEndRow());
        
        int startCol = Math.max(output.getStartCol(), second.getStartCol());
        int endCol   = Math.min(output.getEndCol(), second.getEndCol());
        
        return Rectangle.build(startRow, endRow, startCol, endCol);
    }

    private static int getNextRowGroup(int startFrom, ArrayList<Chunk> firstOpChunks) {
        int end = firstOpChunks.size();
        int currentEndRow = firstOpChunks.get(startFrom).getEndRow();
        int currentStartRow;

        int index = startFrom + 1;

        while (index < end) {
            currentStartRow = firstOpChunks.get(index).getStartRow();
            if (currentEndRow <= currentStartRow) {
                break;
            }
            ++index;
        } //else
        if (index >= end)
            return -1;

        return index;
    }

    private static int getNextColGroup(int startFrom, ArrayList<Chunk> secondOpChunks) {
        int end = secondOpChunks.size();
        int currentEndCol = secondOpChunks.get(startFrom).getEndCol();
        int currentStartCol;

        int index = startFrom + 1;

        while (index < end) {
            currentStartCol = secondOpChunks.get(index).getStartCol();
            if (currentEndCol <= currentStartCol) {
                break;
            }
            ++index;
        } //else
        if (index >= end)
            return -1;

        return index;
    }


    @Override
    protected void sendOrdersToWorkers() {
    }
}
