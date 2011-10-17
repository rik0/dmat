package it.unipr.aotlab.dmat.core.partitions;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.partitions
 * Date: 10/17/11
 * Time: 2:41 PM
 */
public interface Partition {
    // TODO: create checked exception
    int mapToNode(int col, int j) throws IndexOutOfBoundsException;

}
