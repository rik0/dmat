package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.net.rabbitMQ.Messages;
import it.unipr.aotlab.dmat.core.util.ElementType;
import it.unipr.aotlab.dmat.core.util.ForceLoad;

import java.util.EnumMap;

public class SemiRings {
    static EnumMap<ElementType, SemiRing<?>> defaultSemirings = new EnumMap<ElementType, SemiRing<?>>(
            ElementType.class);

    static {
        ForceLoad.listFromFile(Messages.class, "KindOfSemiRings");
    }

    static void addDefaultSemiring(ElementType et, SemiRing<?> sr) {
        defaultSemirings.put(et, sr);
    }

    static public SemiRing<?> defaultSemiring(ElementType et) {
        return SemiRings.defaultSemirings.get(et);
    }

    private SemiRings() {
    }
}
