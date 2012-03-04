package it.unipr.aotlab.dmat.core.formats.dense;

import it.unipr.aotlab.dmat.core.formats.DenseBase;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.Int32Triplet;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DenseInt32 extends DenseBase {
    protected DenseInt32(Chunk hostChunk) {
        super(hostChunk);
    }

    @Override
    public Integer get(int row, int col) {
        int cood = convertCoods(row, col);
        return array.getInt(cood);
    }

    @Override
    public void set(Object value, int row, int col) {
        Integer tvalue = (Integer)value;
        int cood = convertCoods(row, col);

        array.putInt(cood, tvalue);
    }

    @Override
    public Integer getDefault() {
        return 0;
    }

    @Override
    public MatrixPiece getPiece(MatrixPieces.Builder matrixPiece, Rectangle position, boolean isUpdate) {
        return matrixPiece.buildFromChunk(this, position, isUpdate);

    }

    @Override
    public Iterator<Triplet> matrixPieceIterator(Rectangle r) {
        return new DenseInt32Iterator(r);
    }

    private class DenseInt32Iterator implements Iterator<Triplet> {
        int currentRow;
        int currentCol;
        int value;

        int doHasNext;
        int endRow;
        int endCol;

        public DenseInt32Iterator(Rectangle r) {
            this.currentRow = r.startRow;
            this.currentCol = r.endRow;
            this.endRow = r.endRow;
            this.endCol = r.endCol;
            this.doHasNext = -1;
        }

        public int findNext() {
            for (; currentRow < endRow; ++currentRow) {
                for (; currentRow < endCol; ++currentCol) {
                    if (get(currentRow, currentCol) != getDefault())
                        return 1;
                }
            }
            return 0;
        }

        @Override
        public boolean hasNext() {
            if (doHasNext == -1) doHasNext = findNext();

            return doHasNext != 0 ? true : false;
        }

        @Override
        public Int32Triplet next() {
            try {
                if (hasNext())
                    return new Int32Triplet(currentRow, currentCol, value);

                throw new NoSuchElementException();
            } finally {
                doHasNext = -1;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
