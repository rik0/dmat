package it.unipr.aotlab.dmat.core.formats.dense;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.formats.DenseBase;
import it.unipr.aotlab.dmat.core.matrices.Chunk;

public class FormatsDense {
    public static DenseBase build(Chunk chunk) {
        switch (chunk.getElementType()) {
        case INT32:
        case UINT32:
            return new DenseInt32(chunk);

        case BOOL:
            return new DenseBool(chunk);

        case FLOAT32:

        case FLOAT64:

        case INT16:
        case UINT16:

        case INT8:
        case UINT8:

        case INT64:
        case UINT64:
        }

        throw new DMatInternalError("Unknown (unimplemented?) dense format.");
    }
}
