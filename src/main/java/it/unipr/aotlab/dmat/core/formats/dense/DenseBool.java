package it.unipr.aotlab.dmat.core.formats.dense;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.formats.DenseBase;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieces;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;

import java.nio.ByteBuffer;
import java.util.Iterator;

public class DenseBool extends DenseBase {
    protected DenseBool(Chunk hostChunk) {
        super(hostChunk);
    }

    @Override
    protected void allocateArray() {
        this.array = ByteBuffer.allocateDirect((nofElements() + 7) / 8);
    }

    private byte bitValue(int nofBit) {
        switch (nofBit) {
        case 0:
            return 0x1;
        case 1:
            return 0x2;
        case 2:
            return 0x4;
        case 3:
            return 0x8;
        case 4:
            return 0x10;
        case 5:
            return 0x20;
        case 6:
            return 0x40;
        case 7:
            return (byte) 0x80;
        }

        throw new DMatInternalError("Invalid call to bitValue");
    }

    protected boolean getBit(byte b, int bitNo) {
        int bit = bitValue(bitNo);

        return b == (b & bit);
    }

    protected byte setBit(byte b, boolean value, int bitNo) {
        if (value)
            b = (byte) (b | bitValue(bitNo));
        else
            b = (byte) (b & ~bitValue(bitNo));

        return b;
    }

    @Override
    public Boolean get(int row, int col) {
        int coord = convertCoods(row, col);
        int byteno = coord / 8;
        int bitno = coord % 8;

        return getBit(array.get(byteno), bitno);
    }

    @Override
    public void set(Object value, int row, int col) {
        Boolean tvalue = (Boolean)value;

        int coord = convertCoods(row, col);
        int byteno = coord / 8;
        int bitno = coord % 8;

        byte oldByte = array.get(byteno);

        array.put(byteno, setBit(oldByte, tvalue, bitno));
    }

    @Override
    public Boolean getDefault() {
        return false;
    }

    @Override
    public MatrixPiece getPiece(MatrixPieces.Builder matrixPiece, Rectangle position, boolean isUpdate) {
        return matrixPiece.buildFromChunk(this, position, isUpdate);
    }

    @Override
    public Iterator<Triplet> matrixPieceIterator(Rectangle r) {
        throw new DMatInternalError("No matrix piece iterator yet for bools");
    }

    @Override
    public Iterator<Triplet> matrixRowIterator(int columnNo) {
        throw new DMatInternalError("No matrix columniterator yet for bools");
    }

    @Override
    public Iterator<Triplet> matrixColumnIterator(int rowNo) {
        throw new DMatInternalError("No matrix row iterator yet for bools");
    }

}
