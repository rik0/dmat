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

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageAssignChunkToNode;

import java.io.IOException;

/**
 * User: enrico Package: it.unipr.aotlab.dmat.core.partitions Date: 10/17/11
 * Time: 3:44 PM
 */

public class Chunk {
    ChunkDescription.Format format;
    ChunkDescription.ElementType elementType;
    ChunkDescription.SemiRing semiring;
    ChunkDescription.MatricesOnTheWire matricesOnTheWire;
    String matrixId;
    String chunkId;
    int startRow;
    int endRow;
    int startCol;
    int endCol;
    Node assignedTo;

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndCol() {
        return endCol;
    }

    public String getChunkId() {
        return chunkId;
    }

    public ChunkDescription.Format getFormat() {
        return format;
    }

    public ChunkDescription.ElementType getElementType() {
        return elementType;
    }

    public ChunkDescription.SemiRing getSemiring() {
        return semiring;
    }

    public ChunkDescription.MatricesOnTheWire getMatricesOnTheWire() {
        return matricesOnTheWire;
    }

    public void assignChunkToNode(Node node) throws IOException, DMatError {
        if (assignedTo != null) {
            throw new DMatError("This node has been already assigned to "
                    + assignedTo.getNodeId() + ".");
        }

        ChunkDescription.Body chunk = ChunkDescription.Body.newBuilder()
                .setChunkId(chunkId).setEndCol(endCol).setEndRow(endRow)
                .setStartCol(startCol).setStartRow(startRow).setFormat(format)
                .setElementType(elementType).setSemiRing(semiring)
                .setMatricesOnTheWire(matricesOnTheWire).build();

        node.sendMessage(new MessageAssignChunkToNode(chunk));
        assignedTo = node;
    }

    public Chunk(ChunkDescription.Body m) {
        this.chunkId = m.getChunkId();
        this.startRow = m.getStartRow();
        this.endRow = m.getEndRow();
        this.startCol = m.getStartCol();
        this.endCol = m.getEndCol();
        this.elementType = m.getElementType();
        this.format = m.getFormat();
        this.semiring = m.getSemiRing();
        this.matricesOnTheWire = m.getMatricesOnTheWire();
    }

    Chunk(String matrixId, String chunkId, int startRow, int endRow, int startCol, int endCol) {
        this.matrixId = matrixId;
        this.chunkId = chunkId;
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
        this.matricesOnTheWire = ChunkDescription.MatricesOnTheWire.DEFAULTMATRICESONTHEWIRE;

        this.assignedTo = null;

        //filled-in later during matrix validation
        this.format = null;
        this.elementType = null;
        this.semiring = null;

    }

    Chunk splitHorizzonally(String newChunkName, int newChunkStartRow) {
        Chunk newChunk = new Chunk(matrixId, newChunkName, newChunkStartRow, endRow,
                startCol, endCol);
        endRow = newChunkStartRow;

        return newChunk;
    }

    Chunk splitVertically(String newChunkName, int newChunkStartCol) {
        Chunk newChunk = new Chunk(matrixId, newChunkName, startRow, endRow,
                newChunkStartCol, endCol);
        endCol = newChunkStartCol;

        return newChunk;
    }

    public boolean doesManage(int row, int col) {
        return (row >= getStartRow() && row < getEndRow()
                && col >= getStartCol() && col < getEndCol());
    }

    @Override
    public String toString() {
        return super.toString() + " chunkId:" + chunkId + " startRow: "
                + startRow + " endRow: " + endRow + " startCol: " + startCol
                + " endCol: " + endCol;
    }

    void setIfUnsetFormat(Matrices factory) {
        if (this.format == null) {
            this.format = factory.defaultFormat;
        }
    }

    void setIfUnsetMatricesOnTheWire() {
        if (this.matricesOnTheWire == null) {
            this.matricesOnTheWire = ChunkDescription.MatricesOnTheWire.DEFAULTMATRICESONTHEWIRE;
        }
    }

    void validate(Matrices factory) {
        this.elementType = factory.buildingMatrix.elementType;
        this.semiring = factory.buildingMatrix.semiring;
        setIfUnsetFormat(factory);
        setIfUnsetMatricesOnTheWire();
    }
}
