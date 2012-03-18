package it.unipr.aotlab.dmat.core.matrixPiece;

public class DoubleTriplet implements Triplet {
    public int row;
    public int col;
    public double value;

    public DoubleTriplet(int row, int col, double value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    @Override
    public int row() {
        return row;
    }

    @Override
    public int col() {
        return col;
    }

    @Override
    public Double value() {
        return value;
    }

    @Override
    public void setValue(Object newValue) {
        Double tNewValue = (Double)newValue;

        value = tNewValue;
    }

    @Override
    public Triplet getCopy() {
        return new DoubleTriplet(row, col, value);
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public void setCol(int col) {
        this.col = col;
    }
}
