package it.unipr.aotlab.dmat.core;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.matrices.Chunk;

public class InNodeChunks {
    static InNodeChunk<?> build(Chunk c) {
        switch (c.getElementType()) {
        case BOOL:
            return new InNodeChunk<Boolean>(c);

        case INT8:
        case UINT8:
            return new InNodeChunk<Byte>(c);
        case INT16:
        case UINT16:
            return new InNodeChunk<Short>(c);
        case INT32:
        case UINT32:
            return new InNodeChunk<Integer>(c);
        case INT64:
        case UINT64:
            return new InNodeChunk<Long>(c);

        case FLOAT32:
            return new InNodeChunk<Float>(c);
        case FLOAT64:
            return new InNodeChunk<Double>(c);
        }

        throw new DMatInternalError("Unknown Element Type. Chunk: " + c);
    }
}
