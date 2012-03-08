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

        ArrayList<NeededPieceOfChunk> firstOpChunks
            = firstOperand.involvedChunksAllCols(outputMatrixChunk.getStartRow(),
                                                 outputMatrixChunk.getEndRow());

        Collections.sort(firstOpChunks, new Comparator<NeededPieceOfChunk>() {
            @Override
            public int compare(NeededPieceOfChunk lhs, NeededPieceOfChunk rhs) {
                int rv = lhs.chunk.getStartRow() - rhs.chunk.getStartRow();
                if (rv == 0) rv = lhs.chunk.getEndRow() - rhs.chunk.getEndRow();
                return rv;
            }});

        ArrayList<NeededPieceOfChunk> secondOpChunks
            = secondOperand.involvedChunksAllRows(outputMatrixChunk.getStartCol(),
                                                  outputMatrixChunk.getEndCol());

        Collections.sort(secondOpChunks, new Comparator<NeededPieceOfChunk>() {
            @Override
            public int compare(NeededPieceOfChunk lhs, NeededPieceOfChunk rhs) {
                int rv = lhs.chunk.getStartCol() - rhs.chunk.getStartCol();
                if (rv == 0) rv = lhs.chunk.getEndCol() - rhs.chunk.getEndCol();
                return rv;
            }
        });

        return makeWorkzones(outputMatrixChunk, firstOpChunks, secondOpChunks);
    }

    private ArrayList<WorkZone> makeWorkzones(Chunk outputMatrixChunk,
                                              ArrayList<NeededPieceOfChunk> firstOpChunks,
                                              ArrayList<NeededPieceOfChunk> secondOpChunks) {
        /* Creates workzones using first operand row division and
         * second operand columns division. */
        Matrix firstOperand = operands.get(1);
        Matrix secondOperand = operands.get(2);

        ArrayList<WorkZone> workZones = new ArrayList<WorkZone>();

        int rowGroupIndex = 0;
        int colGroupIndex = 0;
        Rectangle outputArea = new Rectangle();

        do {
            // loop over columns
            rowGroupIndex = 0;
            NeededPieceOfChunk secondOpChunk = secondOpChunks.get(colGroupIndex);
            outputArea.startCol = Math.max(outputMatrixChunk.getStartCol(),
                                           secondOpChunk.chunk.getStartCol());
            outputArea.endCol = Math.min(outputMatrixChunk.getEndCol(),
                                         secondOpChunk.chunk.getEndCol());

            ArrayList<NeededPieceOfChunk> secondOpNeededChunks
                = secondOperand.involvedChunksAllRows(outputArea.startCol,
                                                      outputArea.endCol);

            do {
                // loop over rows
                NeededPieceOfChunk firstOpChunk = firstOpChunks.get(rowGroupIndex);
                outputArea.startRow = Math.max(outputMatrixChunk.getStartRow(),
                                               firstOpChunk.chunk.getStartRow());
                outputArea.endRow = Math.min(outputMatrixChunk.getEndRow(),
                                             firstOpChunk.chunk.getEndRow());
                ArrayList<NeededPieceOfChunk> neededChunks = new ArrayList<NeededPieceOfChunk>();

                neededChunks.addAll(firstOperand
                        .involvedChunksAllCols(outputArea.startRow,
                                               outputArea.endRow));
                neededChunks.addAll(secondOpNeededChunks);

                workZones.add(new WorkZone(new Rectangle(outputArea), neededChunks));
            } while (-1 != (rowGroupIndex = getNextRowGroup(rowGroupIndex, firstOpChunks)));
        } while (-1 != (colGroupIndex = getNextColGroup(colGroupIndex, secondOpChunks)));

        return workZones;
    }

    private static int getNextRowGroup(int startFrom, ArrayList<NeededPieceOfChunk> firstOpChunks) {
        int end = firstOpChunks.size();
        int currentEndRow = firstOpChunks.get(startFrom).chunk.getEndRow();
        int currentStartRow;

        int index = startFrom + 1;

        while (index < end) {
            currentStartRow = firstOpChunks.get(index).chunk.getStartRow();
            if (currentEndRow <= currentStartRow) {
                break;
            }
            ++index;
        } //else
        if (index >= end)
            return -1;

        return index;
    }

    private static int getNextColGroup(int startFrom, ArrayList<NeededPieceOfChunk> secondOpChunks) {
        int end = secondOpChunks.size();
        int currentEndCol = secondOpChunks.get(startFrom).chunk.getEndCol();
        int currentStartCol;

        int index = startFrom + 1;

        while (index < end) {
            currentStartCol = secondOpChunks.get(index).chunk.getStartCol();
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
    protected void sendOperationsOrders() {
    }
}
