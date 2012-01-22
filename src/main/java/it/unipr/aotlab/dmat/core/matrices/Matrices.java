/* Copyright */

package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.InvalidMatricesCall;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.initializers.Initializers;
import it.unipr.aotlab.dmat.core.semirings.SemiRings;

/**
 * User: enrico Package: it.unipr.aotlab.dmat.core.matrices Date: 10/17/11 Time:
 * 2:52 PM
 */

// TODO: what do we mean with multiple matrix factories?
// TODO: how to allocate nodes?
public class Matrices {
    public void reset() {
        this.state = 0;
        this.buildingMatrix = new Matrix();
        this.defaultFormat = ChunkDescription.Format.DENSE;
    }

    public static Matrices newBuilder() {
        return new Matrices();
    }

    /* state & 1 means nof columns is set
     * state & 2 means nof rows is set
     * state & 4 means the default chunk has been created */
    int state;
    Matrix buildingMatrix;
    ChunkDescription.Format defaultFormat;

    public Matrices splitHorizzontalyChuck(String splitsChuckName, int row,
            String newChunkName) throws ChunkNotFound {
        return splitHorizzontalyChuck(splitsChuckName, row, splitsChuckName,
                newChunkName);
    }

    public Matrices splitVerticallyChuck(String splitsChuckName, int col,
            String newChunkName) throws ChunkNotFound {
        return splitVerticallyChuck(splitsChuckName, col, splitsChuckName,
                newChunkName);
    }

    public Matrices splitHorizzontalyChuck(String splitsChuckName, int row,
            String oldChunkNewName, String newChunkName) throws ChunkNotFound {
        if (splitsChuckName == null)
            splitsChuckName = "default";

        Chunk oldChunk = buildingMatrix.getChunk(splitsChuckName);
        Chunk newChunk = oldChunk.splitHorizzonally(newChunkName, row);
        buildingMatrix.chunks.add(newChunk);

        oldChunk.chunkId = oldChunkNewName;

        return this;
    }

    public Matrices splitVerticallyChuck(String splitsChuckName, int col,
            String oldChunkNewName, String newChunkName) throws ChunkNotFound {
        if (splitsChuckName == null)
            splitsChuckName = "default";

        Chunk oldChunk = buildingMatrix.getChunk(splitsChuckName);
        Chunk newChunk = oldChunk.splitVertically(newChunkName, col);
        buildingMatrix.chunks.add(newChunk);

        oldChunk.chunkId = oldChunkNewName;

        return this;
    }

    public Matrices setNofColumns(int nofColumns) {
        state |= 1;
        buildingMatrix.cols = nofColumns;
        createDefaultChunk();

        return this;
    }

    public Matrices setNofRows(int nofRows) {
        state |= 2;
        buildingMatrix.rows = nofRows;
        createDefaultChunk();

        return this;
    }

    public Matrices setElementType(ChunkDescription.ElementType elementType) {
        buildingMatrix.elementType = elementType;

        return this;
    }

    public Matrices setDefaultChunkFormat(ChunkDescription.Format defaultFormat) {
        this.defaultFormat = defaultFormat;
        return this;
    }

    public Matrices setChunkFormat(String chunkName,
            ChunkDescription.Format format) throws ChunkNotFound {
        buildingMatrix.getChunk(chunkName).format = format;
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

    private void validateBuildingMatrix() {
        if (buildingMatrix.elementType == null)
            buildingMatrix.elementType = ChunkDescription.ElementType.INT32;

        if (buildingMatrix.semiring == null)
            buildingMatrix.semiring = ChunkDescription.SemiRing.DEFAULTSEMIRING;
            
        if (buildingMatrix.init == null)
            buildingMatrix.init = Initializers
                    .defaultInitializer(buildingMatrix.elementType);

        for (Chunk c : buildingMatrix.chunks) {
            c.elementType = buildingMatrix.elementType;
            c.semiring = buildingMatrix.semiring;
            if (c.format == null) {
                c.format = defaultFormat;
            }
        }
    }

    public Matrix build() {
        validateBuildingMatrix();

        Matrix builtMatrix = buildingMatrix;
        invalidateThisFactory();

        return builtMatrix;
    }

    private Matrices() {
        reset();
    }

    private void invalidateThisFactory() {
        buildingMatrix = null;
    }
}
