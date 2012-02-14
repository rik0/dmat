package it.unipr.aotlab.dmat.core.util;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;

public class TypeInfo {
    ChunkDescriptionWire.ElementType type;

    public TypeInfo(ChunkDescriptionWire.ElementType type) {
        this.type = type;
    }

    public int sizeOf() {
        switch (type) {
        case BOOL:
        case INT8:
        case UINT8:
            return 1;

        case INT16:
        case UINT16:
            return 2;

        case FLOAT32:
        case INT32:
        case UINT32:
            return 4;

        case FLOAT64:
        case INT64:
        case UINT64:
            return 8;
        }

        throw new DMatInternalError("Unknown format.");
    }

    public int absOffset(int index) {
        return index * sizeOf();
    }
}
