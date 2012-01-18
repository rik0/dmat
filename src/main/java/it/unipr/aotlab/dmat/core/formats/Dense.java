package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.MatrixPiece;
import it.unipr.aotlab.dmat.core.net.MatrixPieceTriplets;

import java.util.Vector;

public class Dense<E> implements ChunkAccessor<E> {
    Chunk hostChunk;
    Vector<E> elements;

    Dense(Chunk c) {
        hostChunk = c;
        elements = new Vector<E>((c.getEndRow() - c.getStartCol())
                * (c.getEndCol() - c.getStartCol()));
    }

    @Override
    public E get(int r, int c) {
        int pos = convertCoords(r, c);

        return elements.get(pos);
    }

    @Override
    public void set(E value, int r, int c) {
        int pos = convertCoords(r, c);

        elements.set(pos, value);
    }

    private void doesManage(int r, int c) {
        if (!hostChunk.doesManage(r, c)) {
            throw new DMatInternalError("Chunk received invalid request! "
                    + hostChunk + " Requested: row " + r + " col " + c);
        }
    }

    private int convertCoords(int r, int c) {
        doesManage(r, c);

        r -= hostChunk.getStartRow();
        c -= hostChunk.getStartCol();

        return r * hostChunk.getEndCol() + c;
    }

    @Override
    public MatrixPiece getPiece(int startRow, int endRow, int startCol,
            int endCol) {
        switch (hostChunk.getElementType()) {
        case INT32:
        case UINT32:
            return getPieceInt32Triplets(startRow, endRow, startCol, endCol);
        }

        throw new DMatInternalError("Unknow type.");
    }

    public MatrixPiece getPieceInt32Triplets(int startRow, int endRow,
            int startCol, int endCol) {
        MatrixPieceTripletsInt32.Body.Builder pieceBuilder = MatrixPieceTripletsInt32.Body
                .newBuilder();
        for (int col = startCol; col < endCol; ++col) {
            for (int row = startRow; row < endRow; ++row) {
                Integer v = (Integer) get(row, col);
                if (v != null) {
                    pieceBuilder.addValues(MatrixPieceTripletsInt32.Triplet
                            .newBuilder().setRow(row).setCol(col).setValue(v).build());
                }
            }
        }

        return new MatrixPieceTriplets(pieceBuilder.build());
    }
}
