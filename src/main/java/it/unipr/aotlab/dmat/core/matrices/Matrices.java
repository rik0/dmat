/* Copyright */

package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.InvalidMatricesCall;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.matrices
 * Date: 10/17/11
 * Time: 2:52 PM
 */

// TODO: what do we mean with multiple matrix factories?
// TODO: how to allocate nodes?
public class Matrices {
    /* state & 1 means nof columns is set
     * state & 2 means nof rows is set
     * state & 4 means the default chunk has been created */
    int state = 0;
    Matrix buildingMatrix = new Matrix();

    private void createDefaultChunk() {
        if ((state & 4) == 4) {
            throw new InvalidMatricesCall();
        }

        if ((state & 3) == 3) {
            buildingMatrix.chunks.add(new Chunk("default", 0,
                    buildingMatrix.rows, 0, buildingMatrix.cols));
            state |= 4;
        }
    }

    public Matrices splitHorizzontalyChuck(final String chuckName, final int row) {

        return this;
    }

    public Matrices setNofColumns(final int nofColumns) {
        state |= 1;
        buildingMatrix.cols = nofColumns;
        createDefaultChunk();

        return this;
    }

    public Matrices setNofRows(final int nofRows) {
        state |= 2;
        buildingMatrix.rows = nofRows;
        createDefaultChunk();

        return this;
    }

    public void reset() {
        state = 0;
        buildingMatrix = new Matrix();
    }

    public Matrix build() {
        final Matrix builtMatrix = buildingMatrix;
        reset();

        return builtMatrix;
    }
}
