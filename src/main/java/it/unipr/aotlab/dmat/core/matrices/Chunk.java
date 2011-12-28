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

import java.util.Comparator;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.partitions
 * Date: 10/17/11
 * Time: 3:44 PM
 */

public class Chunk {
    String chunkId;
    int startRow;
    int endRow;
    int startCol;
    int endCol;

    // package visibility
    Chunk(final String chunkId, final int startRow, final int endRow,
            final int startCol, final int endCol) {
        this.chunkId = chunkId;
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    public String getChunkId() {
        return chunkId;
    }

    Chunk splitHorizzonally(final String newChunkName,
            final int newChunkStartRow) {
        final Chunk newChunk = new Chunk(newChunkName, newChunkStartRow,
                endRow, startCol, endCol);
        endRow = newChunkStartRow;

        return newChunk;
    }

    Chunk splitVertically(final String newChunkName,
            final int newChunkStartCol) {
        final Chunk newChunk = new Chunk(newChunkName, startRow, endRow,
                newChunkStartCol, endCol);
        endCol = newChunkStartCol;

        return newChunk;
    }

    static class comparator implements Comparator<Chunk> {
        @Override
        public int compare(final Chunk left, final Chunk right) {
            int comparation = left.startCol - right.startCol;
            if (comparation == 0) {
                comparation = left.startRow - right.startRow;
            }

            return comparation;
        }
    }
}
