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
import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire.ChunkDescriptionBody;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.RectangleWire;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAssignChunkToNode;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageExposeValues;

import java.io.IOException;

/**
 * User: enrico Package: it.unipr.aotlab.dmat.core.partitions Date: 10/17/11
 * Time: 3:44 PM
 */

public class Chunk {
    ChunkDescriptionWire.Format format;
    TypeWire.TypeBody elementType;
    ChunkDescriptionWire.MatricesOnTheWire matricesOnTheWire;
    String matrixId;
    int matrixNofRows;
    int matrixNofColumns;
    String chunkId;
    Matrix hostMatrix;
    Rectangle matrixPosition;

    Node assignedTo; // non-null for master node rep
    String nodeId;   // non-null for chunks on the nodes

    public int getStartRow() {
        return matrixPosition.startRow;
    }

    public int getEndRow() {
        return matrixPosition.endRow;
    }

    public int getStartCol() {
        return matrixPosition.startCol;
    }

    public int getEndCol() {
        return matrixPosition.endCol;
    }

    public TypeWire.TypeBody getType() {
        return elementType;
    }

    public int getMatrixNofRows() {
        return matrixNofRows;
    }

    public int getMatrixNofColumns() {
        return matrixNofColumns;
    }

    public int nofElements() {
        return (matrixPosition.endRow - matrixPosition.startRow)
                * (matrixPosition.endCol - matrixPosition.startCol);
    }

    public String getMatrixId() {
        return matrixId;
    }

    public String getChunkId() {
        return chunkId;
    }

    public String getAssignedNodeId() {
        if (nodeId != null)
            return nodeId;

        return assignedTo.getNodeId();
    }

    public Rectangle getArea() {
        return new Rectangle(matrixPosition);
    }

    public Node getAssignedNode() {
        return assignedTo;
    }

    public ChunkDescriptionWire.Format getFormat() {
        return format;
    }

    public TypeWire.ElementType getElementType() {
        return elementType.getElementType();
    }

    public TypeWire.SemiRing getSemiring() {
        return elementType.getSemiRing();
    }

    public ChunkDescriptionWire.MatricesOnTheWire getMatricesOnTheWire() {
        return matricesOnTheWire;
    }

    public void assignChunkToNode(Node node) throws IOException, DMatError {
        if (assignedTo != null)
            throw new DMatError("This node has been already assigned to "
                    + assignedTo.getNodeId() + ".");

        node.sendMessage(new MessageAssignChunkToNode(this));

        node.addChunk(this);
        assignedTo = node;
    }

    public ChunkDescriptionBody.Builder buildMessageBuilder() {
        RectangleWire.RectangleBody position
                = RectangleWire.RectangleBody.newBuilder()
                .setStartRow(matrixPosition.startRow)
                .setEndRow(matrixPosition.endRow)
                .setStartCol(matrixPosition.startCol)
                .setEndCol(matrixPosition.endCol).build();

        TypeWire.TypeBody type = TypeWire.TypeBody.newBuilder()
                .setElementType(getElementType())
                .setSemiRing(getSemiring()).build();

        return ChunkDescriptionBody.newBuilder()
                .setChunkId(chunkId)
                .setPosition(position)
                .setFormat(format)
                .setType(type)
                .setMatrixId(matrixId)
                .setMatrixNofRows(matrixNofRows)
                .setMatrixNofColumns(matrixNofColumns)
                .setMatricesOnTheWire(matricesOnTheWire);
    }

    public Chunk(ChunkDescriptionWire.ChunkDescriptionBody m, String nodeId) {
        this.chunkId = m.getChunkId();
        this.matrixPosition = Rectangle.build(m.getPosition());
        this.elementType = m.getType();
        this.format = m.getFormat();
        this.matricesOnTheWire = m.getMatricesOnTheWire();
        this.matrixId = m.getMatrixId();
        this.nodeId = nodeId;
        this.matrixNofRows = m.getMatrixNofRows();
        this.matrixNofColumns = m.getMatrixNofColumns();
    }

    Chunk(String matrixId,
          String chunkId,
          Rectangle matrixArea,
          Matrix hostMatrix) {
        this.matrixId = matrixId;
        this.chunkId = chunkId;
        this.matrixPosition = new Rectangle(matrixArea);
        this.matricesOnTheWire = ChunkDescriptionWire
                .MatricesOnTheWire.DEFAULTMATRICESONTHEWIRE;
        this.hostMatrix = hostMatrix;
        this.matrixNofRows = hostMatrix.rows;
        this.matrixNofColumns = hostMatrix.cols;

        this.assignedTo = null;

        //filled-in later during matrix validation
        this.format = null;
        this.elementType = null;
    }

    Chunk splitHorizzonally(String newChunkName, int newChunkStartRow) {
        Chunk newChunk = new Chunk(matrixId,
                                   newChunkName,
                                   matrixPosition,
                                   hostMatrix);

        newChunk.matrixPosition.startRow = newChunkStartRow;
        matrixPosition.endRow = newChunkStartRow;

        return newChunk;
    }

    Chunk splitVertically(String newChunkName, int newChunkStartCol) {
        Chunk newChunk = new Chunk(matrixId,
                                   newChunkName,
                                   matrixPosition,
                                   hostMatrix);

        newChunk.matrixPosition.startCol = newChunkStartCol;
        matrixPosition.endCol = newChunkStartCol;

        return newChunk;
    }

    public boolean doesManage(int row, int col) {
        return (row >= getStartRow() && row < getEndRow()
             && col >= getStartCol() && col < getEndCol());
    }

    @Override
    public String toString() {
        return "Matrix.Chunk Id: " + matrixId + "."+ chunkId + " startRow: "
                + matrixPosition.startRow + " endRow: "
                + matrixPosition.endRow + " startCol: "
                + matrixPosition.startCol
                + " endCol: " + matrixPosition.endCol;
    }

    void setFormat(Matrices factory) {
        if (this.format == null) {
            this.format = factory.defaultFormat;
        }
    }

    void setMatricesOn() {
        if (this.matricesOnTheWire == null) {
            this.matricesOnTheWire = ChunkDescriptionWire
                    .MatricesOnTheWire.DEFAULTMATRICESONTHEWIRE;
        }
    }

    void validate(Matrices factory) {
        this.elementType = factory.type;
        setFormat(factory);
        setMatricesOn();
    }

    public boolean doesIntersectWith(Chunk c) {
        return doesIntersectWith(c.matrixPosition);
    }

    public boolean doesIntersectWith(Rectangle r) {
        return doesIntersectWith(r.startRow, r.endRow, r.startCol, r.endCol);
    }

    public boolean doesIntersectWith(int startRow,
                                     int endRow,
                                     int startCol,
                                     int endCol) {
        return ! (getStartRow() >= endRow || startRow >= getEndRow()
               || getStartCol() >= endCol || startCol >= getEndCol());
    }

    public Rectangle intersection(Chunk c) {
        return intersection(c.matrixPosition);
    }

    public Rectangle intersection(Rectangle r) {
        return intersection(r.startRow, r.endRow, r.startCol, r.endCol);
    }

    public Rectangle intersection(int startRow,
                                  int endRow,
                                  int startCol,
                                  int endCol) {
        if ( ! doesIntersectWith(startRow, endRow, startCol, endCol))
            return null;

        Rectangle or = new Rectangle();

        or.startRow = Math.max(getStartRow(), startRow);
        or.endRow = Math.min(getEndRow(), endRow);

        or.startCol = Math.max(getStartCol(), startCol);
        or.endCol = Math.min(getEndCol(), endCol);

        return or;
    }

    public void sendMessageExposeValues() throws IOException {
        MatrixPieceOwnerBody.Builder mp = MatrixPieceOwnerBody.newBuilder();
        getAssignedNode().sendMessage(new MessageExposeValues(
                mp.setMatrixId(getMatrixId())
                  .setChunkId(getChunkId())));
    }
}
