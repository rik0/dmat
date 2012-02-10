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
import it.unipr.aotlab.dmat.core.generated.RectangleWire;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAssignChunkToNode;

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
    Matrix hostMatrix;
    Rectangle matrixArea;
    Node assignedTo;

    public int getStartRow() {
        return matrixArea.startRow;
    }

    public int getEndRow() {
        return matrixArea.endRow;
    }

    public int getStartCol() {
        return matrixArea.startCol;
    }

    public int getEndCol() {
        return matrixArea.endCol;
    }

    public int nofElements() {
        return (matrixArea.endRow - matrixArea.startRow) * (matrixArea.endCol - matrixArea.startCol);
    }

    public String getChunkId() {
        return chunkId;
    }

    public String getMatrixId() {
        return matrixId;
    }
    
    public Rectangle getArea() {
        return new Rectangle(matrixArea); 
    }

    public Node getAssignedNode() {
        return assignedTo;
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
        if (assignedTo != null)
            throw new DMatError("This node has been already assigned to "
                    + assignedTo.getNodeId() + ".");

        node.sendMessage(new MessageAssignChunkToNode(this));
        assignedTo = node;
    }

    public ChunkDescription.Body buildMessageBody() {
        RectangleWire.RectangleBody position = RectangleWire.RectangleBody.newBuilder()
                .setStartRow(matrixArea.startRow).setEndRow(matrixArea.endRow).setEndCol(matrixArea.endCol)
                .setStartCol(matrixArea.startCol).build();

        return ChunkDescription.Body.newBuilder().setChunkId(chunkId)
                .setPosition(position).setFormat(format)
                .setElementType(elementType).setSemiRing(semiring)
                .setMatrixId(matrixId).setMatricesOnTheWire(matricesOnTheWire)
                .build();
    }

    public Chunk(ChunkDescription.Body m) {
        this.chunkId = m.getChunkId();
        this.matrixArea = Rectangle.build(m.getPosition());
        this.elementType = m.getElementType();
        this.format = m.getFormat();
        this.semiring = m.getSemiRing();
        this.matricesOnTheWire = m.getMatricesOnTheWire();
        this.matrixId = m.getMatrixId();
    }

    Chunk(String matrixId, String chunkId, Rectangle matrixArea, Matrix hostMatrix) {
        this.matrixId = matrixId;
        this.chunkId = chunkId;
        this.matrixArea = new Rectangle(matrixArea);
        this.matricesOnTheWire = ChunkDescription.MatricesOnTheWire.DEFAULTMATRICESONTHEWIRE;
        this.hostMatrix = hostMatrix;

        this.assignedTo = null;

        //filled-in later during matrix validation
        this.format = null;
        this.elementType = null;
        this.semiring = null;

    }

    Chunk splitHorizzonally(String newChunkName, int newChunkStartRow) {
        Chunk newChunk = new Chunk(matrixId, newChunkName, matrixArea, hostMatrix);

        newChunk.matrixArea.startRow = newChunkStartRow;
        matrixArea.endRow = newChunkStartRow;

        return newChunk;
    }

    Chunk splitVertically(String newChunkName, int newChunkStartCol) {
        Chunk newChunk = new Chunk(matrixId, newChunkName, matrixArea, hostMatrix);

        newChunk.matrixArea.startCol = newChunkStartCol;
        matrixArea.endCol = newChunkStartCol;

        return newChunk;
    }

    public boolean doesManage(int row, int col) {
        return (row >= getStartRow() && row < getEndRow()
             && col >= getStartCol() && col < getEndCol());
    }

    @Override
    public String toString() {
        return super.toString() + " chunkId:" + chunkId + " startRow: "
                + matrixArea.startRow + " endRow: " + matrixArea.endRow + " startCol: " + matrixArea.startCol
                + " endCol: " + matrixArea.endCol;
    }

    void setFormat(Matrices factory) {
        if (this.format == null) {
            this.format = factory.defaultFormat;
        }
    }

    void setMatricesOn() {
        if (this.matricesOnTheWire == null) {
            this.matricesOnTheWire = ChunkDescription.MatricesOnTheWire.DEFAULTMATRICESONTHEWIRE;
        }
    }

    void validate(Matrices factory) {
        this.elementType = factory.buildingMatrix.elementType;
        this.semiring = factory.buildingMatrix.semiring;
        setFormat(factory);
        setMatricesOn();
    }

    public boolean doesIntersectWith(Chunk c) {
        return ( ! (getStartRow() >= c.getEndRow()
                 || c.getStartCol() >= getEndCol()
                 || c.getStartRow() >= getEndRow()
                 || getStartCol() >= c.getEndCol()));
    }

    public Rectangle intersection(Chunk c) {
        if (!doesIntersectWith(c)) {
            return null;
        }

        Rectangle r = new Rectangle();

        r.startRow = Math.max(getStartRow(), c.getStartRow());
        r.endRow = Math.min(getEndRow(), c.getEndRow());

        r.startCol = Math.max(getStartCol(), c.getStartCol());
        r.endCol = Math.min(getEndCol(), c.getEndCol());

        return r;
    }
}
