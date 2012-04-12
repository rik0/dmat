package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.TypeWire.SemiRing;
import it.unipr.aotlab.dmat.core.util.TypeInfo;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;

import java.nio.ByteBuffer;

public abstract class DenseBase implements ChunkAccessor {
    protected ByteBuffer array;
    protected InNodeChunk<?> hostChunk;
    protected TypeInfo typeInfo;
    protected int width;
    protected SemiRing semiring;

    @Override
    public InNodeChunk<?> hostChunk() {
        return hostChunk;
    }

    protected DenseBase(InNodeChunk<?> hostChunk) {
        this.typeInfo = new TypeInfo(hostChunk.chunk.getElementType());
        this.hostChunk = hostChunk;
        this.width = hostChunk.chunk.getEndCol()
                - hostChunk.chunk.getStartCol();

        allocateArray();
        resetToZero();
    }

    protected int nofElements() {
        int nofElements = hostChunk.chunk.getEndRow()
                - hostChunk.chunk.getStartRow();

        nofElements *= hostChunk.chunk.getEndCol()
                - hostChunk.chunk.getStartCol();

        return nofElements;
    }

    protected void allocateArray() {
        this.array = ByteBuffer.allocateDirect(
                nofElements() * typeInfo.sizeOf());
    }

    protected void doesManage(int r, int c) {
        if (!hostChunk.chunk.doesManage(r, c)) {
            throw new DMatInternalError("Chunk received invalid request! "
                    + hostChunk + " Requested: row " + r + " col " + c);
        }
    }

    protected int convertCoods(int r, int c) {
        doesManage(r, c);

        r -= hostChunk.chunk.getStartRow();
        c -= hostChunk.chunk.getStartCol();

        return typeInfo.sizeOf() * (r * width + c);
    }
}
