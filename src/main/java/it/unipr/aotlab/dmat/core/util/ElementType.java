package it.unipr.aotlab.dmat.core.util;

import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;

public class ElementType {
    ChunkDescriptionWire.SemiRing semiring;
    ChunkDescriptionWire.ElementType elementType;

    public ElementType(ChunkDescriptionWire.SemiRing semiring,
            ChunkDescriptionWire.ElementType elementType) {
        this.semiring = semiring;
        this.elementType = elementType;
    }
}
