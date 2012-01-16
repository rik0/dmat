package it.unipr.aotlab.dmat.core.formats;

public interface ChunkAccessor<E> {
    public E get(int row, int col);

    public void set(E value, int row, int col);
}
