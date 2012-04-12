package it.unipr.aotlab.dmat.core.formats;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.formats.dense.FormatsDense;
import it.unipr.aotlab.dmat.core.workingnode.InNodeChunk;

public class Formats {
    public static ChunkAccessor build(InNodeChunk<?> chunk) {
        switch (chunk.chunk.getFormat()) {
        case DENSE:
            return FormatsDense.build(chunk);

        case COMPRESSEDCOLUMNS:

        case COMPRESSEDROWS:

        }

        throw new DMatInternalError("Unknown (unimplemented?) format.");
    }
}
