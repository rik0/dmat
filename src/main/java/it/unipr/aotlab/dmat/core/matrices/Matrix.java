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
import it.unipr.aotlab.dmat.core.errors.InvalidCoord;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.initializers.Initializer;

import java.util.ArrayList;

/**
 * User: enrico Package: it.unipr.aotlab.dmat.core.matrices Date: 10/17/11 Time:
 * 2:51 PM
 */
public class Matrix {
    String id;
    int rows = 0;
    int cols = 0;
    TypeWire.ElementType elementType = null;
    Initializer init = null;
    TypeWire.SemiRing semiring = null;

    private ArrayList<Chunk> chunks = new ArrayList<Chunk>();

    public void checkCoords(int row, int col) {
        if (row < 0 || col < 0 || row >= rows || col >= cols)
            throw new InvalidCoord();
    }

    public int getNofRows() {
        return rows;
    }

    public int getNofCols() {
        return cols;
    }

    public String getMatrixId() {
        return id;
    }

    public TypeWire.ElementType getElementType() {
        return elementType;
    }

    public TypeWire.SemiRing getSemiRing() {
        return semiring;
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
        if (chunkName == null)
            chunkName = "default";

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
        Chunk chunk = null;

        for (Chunk c : chunks) {
            if (c.doesManage(row, col)) {
                chunk = c;
                break;
            }
        }
        if (chunk == null) {
            throw new InvalidCoord();
        }

        return chunk;
    }

    public ArrayList<Chunk> getChunks() {
        return chunks;
    }

    static public boolean intersect(Chunk c, int startRow, int endRow,
            int startCol, int endCol) {
        return !(c.getStartRow() >= endRow || startRow >= c.getEndRow()
                || c.getStartCol() >= endCol || startCol >= c.getEndCol());
    }

    public ArrayList<Chunk> involvedChunks(Rectangle r) {
        return involvedChunks(r.startRow, r.endRow, r.startCol, r.endCol);
    }

    public ArrayList<Chunk> involvedChunks(int startRow, int endRow, int startCol,
            int endCol) {
        ArrayList<Chunk> involved = new ArrayList<Chunk>();

        for (Chunk c : getChunks())
            if (intersect(c, startRow, endRow, startCol, endCol))
                involved.add(c);

        return involved;
    }

    public ArrayList<Chunk> involvedChunksAllRows(int startCol, int endCol) {
        return involvedChunks(0, getNofRows(), startCol, endCol);
    }

    public ArrayList<Chunk> involvedChunksAllCols(int startRow, int endRow) {
        return involvedChunks(startRow, endRow, 0, getNofCols());
    }
}
