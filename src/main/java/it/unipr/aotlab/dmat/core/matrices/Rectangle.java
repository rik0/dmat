package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody;

public class Rectangle {
    public int startRow;
    public int endRow;
    public int startCol;
    public int endCol;

    public Rectangle() {
        this.startRow = 0;
        this.endRow = 0;
        this.startCol = 0;
        this.endCol = 0;
    }

    public Rectangle(Rectangle that) {
        this.startRow = that.startRow;
        this.endRow = that.endRow;
        this.startCol = that.startCol;
        this.endCol = that.endCol;
    }

    public boolean isValid() {
        return ! (endRow < startRow || endCol < startCol);
    }

    public boolean isInside(int row, int col) {
        return     (row >= startRow && row < endRow)
                && (col >= startCol && col < endCol);
    }

    public boolean intersect(Rectangle c) {
        return         ! (startRow >= c.endRow
                && c.startCol >= endCol
                && c.startRow >= endRow
                &&        startCol >= c.endCol);
    }

    public boolean isSubset(Rectangle c) {
        return    (startRow >= c.startRow
                && c.endCol >= endCol
                && c.endRow >= endRow
                && startCol >= c.startCol);
    }

    public static Rectangle build(int startRow, int endRow, int startCol, int endCol) {
        Rectangle r = new Rectangle();

        r.startRow = startRow;
        r.endRow = endRow;
        r.startCol = startCol;
        r.endCol = endCol;

        return r;
    }

    public static Rectangle build(RectangleBody wireRectangle) {
        Rectangle r = new Rectangle();

        r.startRow = wireRectangle.getStartRow();
        r.endRow = wireRectangle.getEndRow();
        r.startCol = wireRectangle.getStartCol();
        r.endCol = wireRectangle.getEndCol();

        return r;
    }

    public RectangleBody convertToProto() {
        return RectangleBody.newBuilder()
                .setStartRow(startRow)
                .setEndRow(endRow)
                .setStartCol(startCol)
                .setEndCol(endCol)
                .build();
    }

    @Override
    public String toString() {
        return super.toString() + ". StartRow: " + this.startRow
                + " EndRow: " + this.endRow
                + " StartCol: " + this.startCol
                + " EndCol: " + this.endCol + ".";
    }

    public int compare(Rectangle rhs) {
        int rv = startRow - rhs.startRow;
        if (rv == 0) rv = endRow - rhs.endRow;
        if (rv == 0) rv = startCol - rhs.startCol;
        if (rv == 0) rv = endCol - rhs.endCol;

        return rv;
    }
}
