package it.unipr.aotlab.dmat.core.formats.dense;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.formats.DenseBase;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;

public class FormatsDense {
    public static DenseBase build(InNodeChunk<?> chunk) {
        switch (chunk.chunk.getElementType()) {
        case INT32:
        case UINT32:
            InNodeChunk<Integer> tchunk = (InNodeChunk<Integer>) chunk;
            return new DenseInt32(tchunk);

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
