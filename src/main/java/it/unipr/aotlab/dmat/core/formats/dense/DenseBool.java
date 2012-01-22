package it.unipr.aotlab.dmat.core.formats.dense;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.formats.DenseBase;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPiece;

import java.nio.ByteBuffer;

public class DenseBool extends DenseBase<Boolean> {
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

        throw new DMatInternalError("Invalid call to bitPow");
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
    public void set(Boolean value, int row, int col) {
        int coord = convertCoods(row, col);
        int byteno = coord / 8;
        int bitno = coord % 8;

        byte oldByte = array.get(byteno);

        array.put(byteno, setBit(oldByte, value, bitno));
    }

    @Override
    public MatrixPiece getPiece(int startRow, int endRow, int startCol,
            int endCol) {
        // TODO Auto-generated method stub
        return null;
    }

}
