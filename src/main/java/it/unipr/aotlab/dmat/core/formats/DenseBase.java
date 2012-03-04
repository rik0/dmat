package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.util.TypeInfo;

import java.nio.ByteBuffer;

public abstract class DenseBase implements ChunkAccessor {
    protected ByteBuffer array;
    protected Chunk hostChunk;
    protected TypeInfo typeInfo;
    protected int width;
    
    public Chunk hostChunk() {
        return hostChunk;
    }

    protected DenseBase(Chunk hostChunk) {
        this.typeInfo = new TypeInfo(hostChunk.getElementType());
        this.hostChunk = hostChunk;
        this.width = hostChunk.getEndCol() - hostChunk.getStartCol();

        allocateArray();
    }

    protected int nofElements() {
        int nofElements = hostChunk.getEndRow() - hostChunk.getStartRow();
        nofElements *= hostChunk.getEndCol() - hostChunk.getStartCol();

        return nofElements;
    }

    protected void allocateArray() {
        this.array = ByteBuffer.allocateDirect(nofElements() * typeInfo.sizeOf());
    }

    protected void doesManage(int r, int c) {
        if (!hostChunk.doesManage(r, c)) {
            throw new DMatInternalError("Chunk received invalid request! "
                    + hostChunk + " Requested: row " + r + " col " + c);
        }
    }

    protected int convertCoods(int r, int c) {
        doesManage(r, c);

        r -= hostChunk.getStartRow();
        c -= hostChunk.getStartCol();

        return typeInfo.sizeOf() * (r * width + c);
    }
}
