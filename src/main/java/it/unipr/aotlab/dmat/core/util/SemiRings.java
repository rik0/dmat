package it.unipr.aotlab.dmat.core.util;

import java.util.EnumMap;

public class SemiRings {
    static EnumMap<ElementType, SemiRing<?>> defaultSemiRings = new EnumMap<ElementType, SemiRing<?>>(
            ElementType.class);

    static void addFactory(final ElementType et, final SemiRing<?> sr) {
        defaultSemiRings.put(et, sr);
    }
    
    static SemiRing<?> semiRing(ElementType et) {
        return SemiRings.defaultSemiRings.get(et);
    }
}
