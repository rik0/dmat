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
        if (r == null)
            r = this.hostChunk.getArea();
        return new DenseInt32IteratorPosition(r);
    }

    private class DenseInt32IteratorPosition implements Iterator<Triplet> {
        int currentRow;
        int startCol;
        int currentCol;
        int value;

        int doHasNext;
        int endRow;
        int endCol;

        public DenseInt32IteratorPosition(Rectangle r) {
            this.currentRow = r.startRow;
            this.currentCol = r.startCol - 1;
            this.startCol = r.startCol;
            this.endRow = r.endRow;
            this.endCol = r.endCol;
            this.doHasNext = -1;
        }

        public int findNext() {
            ++currentCol;

            for (; currentRow < endRow; ++currentRow) {
                for (; currentCol < endCol; ++currentCol) {
                    if ((value = get(currentRow, currentCol)) != getDefault()) {
                        return 1;
                    }
                }

                currentCol = startCol;
            }

            return 0;
        }

        @Override
        public boolean hasNext() {
            if (doHasNext == -1) doHasNext = findNext();

            return doHasNext != 0;
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

    @Override
    public Iterator<Triplet> matrixRowIterator(int rowNo) {
        //TODO we can do better
        Rectangle r = Rectangle.build(rowNo,
                                      rowNo + 1,
                                      hostChunk.getStartCol(),
                                      hostChunk.getEndCol());
        return new DenseInt32IteratorPosition(r);
    }

    @Override
    public Iterator<Triplet> matrixColumnIterator(int colNo) {
        Rectangle r = Rectangle.build(hostChunk.getStartRow(),
                                      hostChunk.getEndRow(),
                                      colNo,
                                      colNo + 1);
        return new DenseInt32IteratorPosition(r);
    }

    @Override
    public void setPosition(Object value, Rectangle position) {
        Integer tvalue = (Integer)value;

        for (int r = position.startRow, er = position.endRow; r < er; ++r) {
            for (int c = position.startCol, ec = position.endCol; c < ec; ++c) {
                int cood = convertCoods(r, c);
                array.putInt(cood, tvalue);
            }
        }
    }

    @Override
    public void set(Triplet t) {
        set(t.value(), t.row(), t.col());
    }
}
