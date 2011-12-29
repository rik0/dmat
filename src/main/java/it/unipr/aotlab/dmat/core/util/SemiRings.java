package it.unipr.aotlab.dmat.core.util;

import java.util.EnumMap;

public class SemiRings {
    static EnumMap<ElementType, SemiRing<?>> defaultSemirings = new EnumMap<ElementType, SemiRing<?>>(
            ElementType.class);

    static void addDefaultSemiring(final ElementType et, final SemiRing<?> sr) {
        defaultSemirings.put(et, sr);
    }
    
    static public SemiRing<?> defaultSemiring(ElementType et) {
        return SemiRings.defaultSemirings.get(et);
    }
}
