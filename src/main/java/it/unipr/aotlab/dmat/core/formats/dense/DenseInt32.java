package it.unipr.aotlab.dmat.core.formats.dense;

import it.unipr.aotlab.dmat.core.formats.DenseBase;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;

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
    public MatrixPiece getPiece(MatrixPieces.Builder matrixPiece,
            int startRow, int endRow, int startCol, int endCol) {
        return matrixPiece.buildFromChunk(this, startRow, startCol, endRow, endCol);
    }
}
