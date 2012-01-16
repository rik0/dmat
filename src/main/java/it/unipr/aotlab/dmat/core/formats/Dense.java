package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.matrices.Chunk;

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
}
