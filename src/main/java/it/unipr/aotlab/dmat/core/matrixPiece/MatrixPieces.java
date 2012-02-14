package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixValues;
import it.unipr.aotlab.dmat.core.util.ForceLoad;

import java.util.EnumMap;

public class MatrixPieces {
    static EnumMap<ChunkDescriptionWire.ElementType, MatrixPieces.Builder> defaultMatrixPieces = new EnumMap<ChunkDescriptionWire.ElementType, MatrixPieces.Builder>(
            ChunkDescriptionWire.ElementType.class);

    static EnumMap<ChunkDescriptionWire.MatricesOnTheWire, MatrixPieces.Builder> matrixPieces = new EnumMap<ChunkDescriptionWire.MatricesOnTheWire, MatrixPieces.Builder>(
            ChunkDescriptionWire.MatricesOnTheWire.class);

    static {
        ForceLoad.listFromFile(MatrixPieces.class, "KindOfMatrixPieces");
    }

    public interface Builder {
        MatrixPiece buildFromMessageBody(Object messageBody);

        <E> MatrixPiece buildFromChunk(ChunkAccessor<E> format, int startRow, int startCol,
                int endRow, int endCol);
        
        <E> MessageMatrixValues buildMessage(MatrixPiece matrixPiece);
    }

    public static Builder defaultMatrixPiece(
            ChunkDescriptionWire.ElementType elementType) {
        return defaultMatrixPieces.get(elementType);
    }

    public static Builder matrixPiece(
            ChunkDescriptionWire.MatricesOnTheWire matricesOnTheWire) {
        return matrixPieces.get(matricesOnTheWire);
    }

    private MatrixPieces() {
    }
}
