package it.unipr.aotlab.dmat.core.partitions;

public class PartitionCyclicRow implements Partition {
    private final int nofNodes;
    private final int nofRows;
    private final int nofColumns;

    public PartitionCyclicRow(final int nofNodes, final int nofRows,
            final int nofColumns) {
        this.nofNodes = nofNodes;
        this.nofRows = nofRows;
        this.nofColumns = nofColumns;
    }

    @Override
    public int mapToNode(int col, int row) throws IndexOutOfBoundsException {
        if (col < 0 || row < 0 || col >= nofColumns || row >= nofRows)
            throw new IndexOutOfBoundsException();
        
        return row % nofNodes;
    }
}
