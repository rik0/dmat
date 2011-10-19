package it.unipr.aotlab.dmat.core.partitions;

public class PartitionRowBlocks implements Partition {
    private final int nofNodes;
    private final int nofRows;
    private final int nofColumns;

    private final int nofRowsPerNode;
    private final int nofResidueRows;

    private final int lineOfChange;

    public PartitionRowBlocks(final int nofNodes, final int nofRows,
            final int nofColumns) {
        this.nofNodes = nofNodes;
        this.nofRows = nofRows;
        this.nofColumns = nofColumns;

        nofRowsPerNode = nofRows / nofNodes;
        nofResidueRows = nofRows % nofNodes;

        lineOfChange = nofResidueRows + nofResidueRows * nofRowsPerNode;
    }

    @Override
    public int mapToNode(final int col, final int row)
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
