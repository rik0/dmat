package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire.Triplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CompressedRowsInt32 {
    int startRow;
    ArrayList<Integer> rows;
    ArrayList<Integer> cols;
    ArrayList<Integer> values;

    private int addRow(List<Triplet> values,
                       int valuesSize,
                       int point,
                       int refRow) {
        Triplet triplet;

        while (point < valuesSize
            && refRow == (triplet = values.get(point)).getRow()) {

            this.cols.add(triplet.getCol());
            this.values.add(triplet.getValue());
            ++point;
        }

        return point;
    }

    private void loopOverTriplets(List<Triplet> values) {
        int size = values.size();
        int reachedPoint = 0;
        int refRow = startRow;

        rows.add(0);
        while (reachedPoint < size) {
            reachedPoint = addRow(values, size, reachedPoint, refRow++);
            rows.add(reachedPoint);
        }
    }

    public class RowIterator implements Iterator<Int32Triplet> {
        int row;
        int current;
        int max;
        int hasNext = -1;

        public RowIterator(int row) {
            this.row = row - startRow;
            this.current = rows.get(this.row) - 1;
            this.max = rows.get(this.row + 1);
        }

        private void findNext() {
            ++current;
            if (current < max)
                hasNext = 1;
            else
                hasNext = 0;
        }

        @Override
        public boolean hasNext() {
            if (hasNext == -1) {
                findNext();
            }

            return hasNext != 0;
        }

        @Override
        public Int32Triplet next() {
            if ( ! hasNext())
                throw new NoSuchElementException();
            hasNext = -1;

            return new Int32Triplet(startRow + row, cols.get(current), values.get(current));
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    Iterator<Int32Triplet> getRowIterator(int row) {
        return new RowIterator(row);
    }

    public CompressedRowsInt32(MatrixPieceTripletsInt32Body triplets) {
        this.startRow = triplets.getPosition().getStartRow();
        int nofRows = triplets.getPosition().getEndRow() - this.startRow;

        this.rows = new ArrayList<Integer>(nofRows + 1);
        this.cols = new ArrayList<Integer>();
        this.values = new ArrayList<Integer>();

        List<Triplet> values = triplets.getValuesList();
        Collections.sort(values, new Comparator<Triplet>() {
            @Override
            public int compare(Triplet lhs, Triplet rhs) {
                int rv = lhs.getRow() - rhs.getRow();
                if (rv == 0)
                    rv = lhs.getCol() - rhs.getCol();
                return rv;
            }
        });

        loopOverTriplets(values);
    }
}
