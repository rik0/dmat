package it.unipr.aotlab.dmat.core.generated;

public class MatrixPieceTripletsInt32WireSupport {
    public static String getMatrixName(MatrixPieceTripletsInt32Wire.Body body) {
        return MatrixPieceTripletsInt32WireSupport.getMatrixName(body);
    }
    
    public static int getColRep(MatrixPieceTripletsInt32Wire.Body body) {
        return MatrixPieceTripletsInt32WireSupport.getColRep(body);
    }

    public static int getRowRep(MatrixPieceTripletsInt32Wire.Body body) {
        return MatrixPieceTripletsInt32WireSupport.getRowRep(body);
    }
    
    public static String sToString(MatrixPieceTripletsInt32Wire.Body body) {
        if (body.getValuesCount() > 0) {
            MatrixPieceTripletsInt32Wire.Triplet t = body.getValues(0);
            return " (" + t.getRow() + ", " + t.getCol() + ") : " + t.getValue();
        }
        return "";
    }
}
