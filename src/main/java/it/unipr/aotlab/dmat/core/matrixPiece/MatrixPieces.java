package it.unipr.aotlab.dmat.core.matrixPiece;

import it.unipr.aotlab.dmat.core.formats.ChunkAccessor;
import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMatrixValues;
import it.unipr.aotlab.dmat.core.util.ForceLoad;

import java.util.EnumMap;

public class MatrixPieces {
    static EnumMap<TypeWire.ElementType, MatrixPieces.Builder> defaultMatrixPieces = new EnumMap<TypeWire.ElementType, MatrixPieces.Builder>(
            TypeWire.ElementType.class);

    static EnumMap<ChunkDescriptionWire.MatricesOnTheWire, MatrixPieces.Builder> matrixPieces = new EnumMap<ChunkDescriptionWire.MatricesOnTheWire, MatrixPieces.Builder>(
            ChunkDescriptionWire.MatricesOnTheWire.class);

    static {
        ForceLoad.listFromFile(MatrixPieces.class, "KindOfMatrixPieces");
    }

    public interface Builder {
        MatrixPiece buildFromMessageBody(Object messageBody);

        MessageMatrixValues buildMessage(MatrixPiece matrixPiece);

        MatrixPiece buildFromChunk(ChunkAccessor format, Rectangle position, boolean isUpdate);
    }

    public static Builder defaultMatrixPiece(
            TypeWire.ElementType elementType) {
        return defaultMatrixPieces.get(elementType);
    }

    public static Builder matrixPiece(
            ChunkDescriptionWire.MatricesOnTheWire matricesOnTheWire) {
        return matrixPieces.get(matricesOnTheWire);
    }

    private MatrixPieces() {
    }
}
