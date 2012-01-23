package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.util.ForceLoad;

import java.util.EnumMap;

public class MatrixPieces {
    static EnumMap<ChunkDescription.ElementType, MatrixPieces.Builder> defaultMatrixPieces = new EnumMap<ChunkDescription.ElementType, MatrixPieces.Builder>(
            ChunkDescription.ElementType.class);

    static EnumMap<ChunkDescription.MatricesOnTheWire, MatrixPieces.Builder> matrixPieces = new EnumMap<ChunkDescription.MatricesOnTheWire, MatrixPieces.Builder>(
            ChunkDescription.MatricesOnTheWire.class);

    static {
        ForceLoad.listFromFile(MatrixPieces.class, "KindOfMatrixPieces");
    }

    public interface Builder {
        MatrixPiece build(Object messageBody); 
    }

    public static Builder defaultMatrixPiece(
            ChunkDescription.ElementType elementType) {
        return defaultMatrixPieces.get(elementType);
    }

    public static Builder matrixPiece(
            ChunkDescription.MatricesOnTheWire matricesOnTheWire) {
        return matrixPieces.get(matricesOnTheWire);
    }

    private MatrixPieces() {
    }
}
