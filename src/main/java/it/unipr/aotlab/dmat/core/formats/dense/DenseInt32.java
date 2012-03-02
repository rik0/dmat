package it.unipr.aotlab.dmat.core.formats.dense;

import it.unipr.aotlab.dmat.core.formats.DenseBase;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.Int32Triplet;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DenseInt32 extends DenseBase<Integer> {
    protected DenseInt32(Chunk hostChunk) {
        super(hostChunk);
    }

    @Override
    public Integer get(int row, int col) {
        int cood = convertCoods(row, col);
        return array.getInt(cood);
    }

    @Override
    public void set(Integer value, int row, int col) {
        int cood = convertCoods(row, col);

        array.putInt(cood, value);
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
    public Iterator<Int32Triplet> matrixPieceIterator() {
        return new DenseInt32Iterator();
    }

    private class DenseInt32Iterator implements Iterator<Int32Triplet> {
        int currentRow = hostChunk.getStartRow();
        int currentCol = hostChunk.getStartCol();
        int value;

        int doHasNext = -1;
        int endRow = hostChunk.getEndRow();
        int endCol = hostChunk.getEndCol();

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
            if (doHasNext == -1) {
                doHasNext = findNext();
            }

            return doHasNext != 0 ? true : false;
        }

        @Override
        public Int32Triplet next() {
            if (doHasNext == -1) {
                doHasNext = findNext();
            }

            try {
                if (doHasNext == 1)
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
