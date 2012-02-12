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

        ArrayList<Chunk> secondOpChunks
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

    private ArrayList<WorkZone> makeWorkzones(Chunk outputMatrixChunk,
                                              ArrayList<Chunk> firstOpChunks,
                                              ArrayList<Chunk> secondOpChunks) {
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
                                           secondOpChunk.getEndRow());

            do {
                Chunk firstOpChunk = firstOpChunks.get(rowGroupIndex);
                ArrayList<Chunk> neededChunks = new ArrayList<Chunk>();

                neededChunks.add(outputMatrixChunk);
                neededChunks.addAll(firstOperand
                        .involvedChunksAllCols(firstOpChunk.getStartRow(),
                                               firstOpChunk.getEndRow()));
                neededChunks.addAll(secondOpNeededChunks);

                Rectangle outputArea = Rectangle.build(firstOpChunk.getStartRow(),
                                                       firstOpChunk.getEndRow(),
                                                       secondOpChunk.getStartCol(),
                                                       secondOpChunk.getEndCol());

                workZones.add(new WorkZone(outputArea, neededChunks));
            } while (-1 != (rowGroupIndex = getNextRowGroup(rowGroupIndex, firstOpChunks)));
        } while (-1 != (colGroupIndex = getNextColGroup(colGroupIndex, secondOpChunks)));

        return workZones;
    }

    private int getNextRowGroup(int startFrom, ArrayList<Chunk> firstOpChunks) {
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
        }

        if (index >= end)
            return -1;

        return index;
    }

    private int getNextColGroup(int startFrom, ArrayList<Chunk> secondOpChunks) {
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
        }

        if (index >= end)
            return -1;

        return index;
    }


    @Override
    protected void sendOrdersToWorkers() {
        // TODO Auto-generated method stub
    }
}
