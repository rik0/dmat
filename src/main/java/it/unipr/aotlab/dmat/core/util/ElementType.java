package it.unipr.aotlab.dmat.core.util;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;

public class ElementType {
    ChunkDescription.SemiRing semiring;
    ChunkDescription.ElementType elementType;

    public ElementType(ChunkDescription.SemiRing semiring,
            ChunkDescription.ElementType elementType) {
        this.semiring = semiring;
        this.elementType = elementType;
    }
}
