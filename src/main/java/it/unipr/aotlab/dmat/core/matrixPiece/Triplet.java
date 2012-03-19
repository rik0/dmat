package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.util.Assertion;


public interface Triplet {
    public static class Comparator implements java.util.Comparator<Triplet> {
        @Override
        public int compare(Triplet lhs, Triplet rhs) {
            Assertion.isTrue(lhs != null && rhs != null, "Cannot compare nulls!");

            int rv = lhs.row() - rhs.row();
            if (rv == 0) rv = lhs.col() - rhs.col();

            return rv;
        }
    }

    public int row();
    public int col();
    public Object value();
    
    public void setValue(Object sv);
    public void setRow(int row);
    public void setCol(int col);

    public Triplet getCopy();
}
