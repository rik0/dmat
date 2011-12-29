/* Copyright */

package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.InvalidMatricesCall;
import it.unipr.aotlab.dmat.core.initializers.Initializers;
import it.unipr.aotlab.dmat.core.util.ElementType;
import it.unipr.aotlab.dmat.core.util.SemiRings;

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

    public Matrices splitHorizzontalyChuck(final String splitsChuckName,
            final int row, final String newChunkName) throws ChunkNotFound {
        return splitHorizzontalyChuck(splitsChuckName, row, splitsChuckName,
                newChunkName);
    }

    public Matrices splitVerticallyChuck(final String splitsChuckName,
            final int col, final String newChunkName) throws ChunkNotFound {
        return splitVerticallyChuck(splitsChuckName, col, splitsChuckName,
                newChunkName);
    }

    public Matrices splitHorizzontalyChuck(final String splitsChuckName,
            final int row, final String oldChunkNewName,
            final String newChunkName) throws ChunkNotFound {
        final Chunk oldChunk = buildingMatrix.getChunk(splitsChuckName);
        buildingMatrix.chunks.add(new Chunk(newChunkName, row, oldChunk.endRow,
                oldChunk.startCol, oldChunk.endRow));
        oldChunk.chunkId = oldChunkNewName;
        oldChunk.endRow = row;

        return this;
    }

    public Matrices splitVerticallyChuck(final String splitsChuckName,
            final int col, final String oldChunkNewName,
            final String newChunkName) throws ChunkNotFound {
        final Chunk oldChunk = buildingMatrix.getChunk(splitsChuckName);
        buildingMatrix.chunks.add(new Chunk(newChunkName, oldChunk.startRow,
                oldChunk.endRow, col, oldChunk.endRow));
        oldChunk.chunkId = oldChunkNewName;
        oldChunk.endCol = col;

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

    public Matrices setElementType(final ElementType elementType) {
        buildingMatrix.elementType = elementType;

        return this;
    }

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

    private void setMissingDefaults() {
        if (buildingMatrix.elementType == null)
            buildingMatrix.elementType = ElementType.INT32;

        if (buildingMatrix.semiring == null)
            buildingMatrix.semiring = SemiRings
                    .defaultSemiring(buildingMatrix.elementType);

        if (buildingMatrix.init == null)
            buildingMatrix.init = Initializers
                    .defaultInitializer(buildingMatrix.elementType);
    }

    public void reset() {
        state = 0;
        buildingMatrix = new Matrix();
    }

    public Matrix build() {
        setMissingDefaults();

        final Matrix builtMatrix = buildingMatrix;
        reset();

        return builtMatrix;
    }
}
