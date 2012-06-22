package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire.Triplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CompressedColsInt32 {
    int startCol;
    ArrayList<Integer> rows;
    ArrayList<Integer> cols;
    ArrayList<Integer> values;

    private int addCol(List<Triplet> values,
                       int valuesSize,
                       int point,
                       int refCol) {
        Triplet triplet;

        while (point < valuesSize
            && refCol == (triplet = values.get(point)).getCol()) {

            this.rows.add(triplet.getRow());
            this.values.add(triplet.getValue());
            ++point;
        }

        return point;
    }

    private void loopOverTriplets(List<Triplet> values) {
        int size = values.size();
        int reachedPoint = 0;
        int refCol = startCol;

        cols.add(0);
        while (reachedPoint < size) {
            reachedPoint = addCol(values, size, reachedPoint, refCol++);
            cols.add(reachedPoint);
        }
    }

    public class ColIterator implements Iterator<Int32Triplet> {
        int col;
        int current;
        int max;
        int hasNext = -1;

        public ColIterator(int col) {
            this.col = col - startCol;
            this.current = cols.get(this.col) - 1;
            this.max = cols.get(this.col + 1);
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

            return new Int32Triplet(rows.get(current), startCol + col, values.get(current));
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    Iterator<Int32Triplet> getColIterator(int col) {
        return new ColIterator(col);
    }

    public CompressedColsInt32(MatrixPieceTripletsInt32Body triplets) {
        this.startCol = triplets.getPosition().getStartCol();
        int nofCols = triplets.getPosition().getEndRow() - this.startCol;

        this.rows = new ArrayList<Integer>();
        this.cols = new ArrayList<Integer>(nofCols+1);
        this.values = new ArrayList<Integer>();

        List<Triplet> values = triplets.getValuesList();
        Collections.sort(values, new Comparator<Triplet>() {
            @Override
            public int compare(Triplet lhs, Triplet rhs) {
                int rv = lhs.getCol() - rhs.getCol();
                if (rv == 0)
                    rv = lhs.getRow() - rhs.getRow();
                return rv;
            }
        });

        loopOverTriplets(values);
    }
}
