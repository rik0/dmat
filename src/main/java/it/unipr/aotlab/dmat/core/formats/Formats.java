package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.matrices.Chunk;

public class Formats {
    public static <E> ChunkAccessor<E> setFormat(ChunkDescription.Format f, Chunk c) {
        switch (f) {
        case DENSE:
            return new Dense<E>(c);
        }

        throw new DMatInternalError("Unknown format.");
    }

    public static <E> ChunkAccessor<E> setFormat(int f, Chunk c) {
        return setFormat(ChunkDescription.Format.valueOf(f), c);
    }
}
