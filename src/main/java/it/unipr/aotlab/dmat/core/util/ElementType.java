package it.unipr.aotlab.dmat.core.util;

import it.unipr.aotlab.dmat.core.generated.TypeWire;

public class ElementType {
    TypeWire.SemiRing semiring;
    TypeWire.ElementType elementType;

    public ElementType(TypeWire.SemiRing semiring,
            TypeWire.ElementType elementType) {
        this.semiring = semiring;
        this.elementType = elementType;
    }
}
