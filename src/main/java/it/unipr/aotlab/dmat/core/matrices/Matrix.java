/*
 * Copyright (c) 2011. Enrico Franchi <efranchi@ce.unipr.it>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.errors.InvalidCoord;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.initializers.Initializer;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;

import java.util.Vector;

/**
 * User: enrico Package: it.unipr.aotlab.dmat.core.matrices Date: 10/17/11 Time:
 * 2:51 PM
 */
public class Matrix {
    int rows = 0;
    int cols = 0;
    ChunkDescription.ElementType elementType = null;
    Initializer init = null;
    SemiRing<?> semiring = null;

    Vector<Chunk> chunks = new Vector<Chunk>();

    public void checkCoords(int row, int col) {
        if (row < 0 || col < 0 || row >= rows || col >= cols)
            throw new InvalidCoord();
    }

    /* TODO: define good format to specify initialization of matrix
     * we probably need something generic like:
     * 1. specifiy values directly
     * 2. specify an URI from which to get the values (URI means that
     *    it can be either a local file for the specific node where the
     *    nodes should be stored or other stuff
     * 3. do we need something clever to manage the fact that some nodes
     *    may want to use different URIs to get their values?
     * 4. what about the formats in which we store the values in the files?
     */

    /**
     * Initializes the matrix with the specified initializer.
     * 
     * @return this
     */
    Matrix initialize() {
        //TODO
        return this;
    }

    public Chunk getChunk(String chunkName) throws ChunkNotFound {
        // Linear search; there must be a better way. But
        // we need to be able to change the keys (chunkId)
        Chunk chunk = null;
        for (Chunk t : chunks) {
            if (t.chunkId.equals(chunkName)) {
                chunk = t;
                break;
            }
        }
        if (chunk == null) {
            throw new ChunkNotFound();
        }

        return chunk;
    }

    public Chunk getChunk(int row, int col) {
        checkCoords(row, col);

        Chunk chunk = null;
        for (Chunk t : chunks) {
            if (row >= t.startRow && row < t.endRow && col >= t.startCol
                    && col < t.endCol) {
                chunk = t;
                break;
            }
        }
        if (chunk == null)
            throw new DMatInternalError();

        return chunk;
    }
}
