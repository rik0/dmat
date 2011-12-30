package it.unipr.aotlab.dmat.core.partitions;

public class PartitionRowBlocks implements Partition {
    private int nofRows;
    private int nofColumns;

    private int nofRowsPerNode;
    private int nofResidueRows;

    private int lineOfChange;

    public PartitionRowBlocks(int nofNodes, final int nofRows,
            int nofColumns) {
        this.nofRows = nofRows;
        this.nofColumns = nofColumns;

        nofRowsPerNode = nofRows / nofNodes;
        nofResidueRows = nofRows % nofNodes;

        lineOfChange = nofResidueRows + nofResidueRows * nofRowsPerNode;
    }

    @Override
    public int mapToNode(int col, final int row)
            throws IndexOutOfBoundsException {
        if (col < 0 || row < 0 || col >= nofColumns || row >= nofRows)
            throw new IndexOutOfBoundsException();

        if (row < lineOfChange) {
            return row / (nofRowsPerNode + 1);
        } else {
            return (row - nofResidueRows) / nofRowsPerNode;
        }
    }
}
