package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.matrices.Chunk;

public class Formats {
    public static <E> ChunkAccessor<?> setFormat(
            ChunkDescription.ElementType type, ChunkDescription.Format f,
            Chunk c) {
        switch (type) {
        case BOOL:
            return Formats.<Boolean> setFormat(f, c);

        case INT8:
        case UINT8:
            return Formats.<Byte> setFormat(f, c);
        case INT16:
        case UINT16:
            return Formats.<Character> setFormat(f, c);
        case INT32:
        case UINT32:
            return Formats.<Integer> setFormat(f, c);
        case INT64:
        case UINT64:
            return Formats.<Long> setFormat(f, c);

        case FLOAT32:
            return Formats.<Float> setFormat(f, c);
        case FLOAT64:
            return Formats.<Double> setFormat(f, c);
        }

        throw new DMatInternalError("Unknown format.");
    }

    private static <E> ChunkAccessor<E> setFormat(ChunkDescription.Format f,
            Chunk c) {
        switch (f) {
        case DENSE:
            return new Dense<E>(c);
        }

        throw new DMatInternalError("Unknown format.");
    }
}
