package it.unipr.aotlab.dmat.core.generated.support;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body;

public class MatrixPieceTripletsInt32WireSupport {

    public static String getMatrixId(MatrixPieceTripletsInt32Body body) {
        return body.getMatrixId();
    }

    public static int getColRep(MatrixPieceTripletsInt32Body body) {
        if (body.getValuesCount() > 0) {
            return body.getValues(0).getCol();
        }
        return -1;
    }

    public static int getRowRep(MatrixPieceTripletsInt32Body body) {
        if (body.getValuesCount() > 0) {
            return body.getValues(0).getRow();
        }
        return -1;
    }

    public static String sToString(MatrixPieceTripletsInt32Body body) {
        String str = body.getMatrixId();
        if (body.getValuesCount() > 0) {
            MatrixPieceTripletsInt32Wire.Triplet t = body.getValues(0);
            str += " (" + t.getRow() + ", " + t.getCol() + ") : "
                    + t.getValue();
        }
        return str;
    }
}
