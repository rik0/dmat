package it.unipr.aotlab.dmat.core.partitions;

public class PartitionCyclicRow implements Partition {
    private int nofNodes;
    private int nofRows;
    private int nofColumns;

    public PartitionCyclicRow(int nofNodes, final int nofRows,
            int nofColumns) {
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
