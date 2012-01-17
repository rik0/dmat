package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Messages;
import it.unipr.aotlab.dmat.core.util.ForceLoad;

import java.util.EnumMap;

public class SemiRings {
    static EnumMap<ChunkDescription.ElementType, SemiRing<?>> defaultSemirings = new EnumMap<ChunkDescription.ElementType, SemiRing<?>>(
            ChunkDescription.ElementType.class);

    static EnumMap<ChunkDescription.SemiRing, SemiRing<?>> semirings = new EnumMap<ChunkDescription.SemiRing, SemiRing<?>>(
            ChunkDescription.SemiRing.class);

    static {
        ForceLoad.listFromFile(Messages.class, "KindOfSemiRings");
    }

    static public SemiRing<?> defaultSemiring(ChunkDescription.ElementType et) {
        return SemiRings.defaultSemirings.get(et);
    }

    static public SemiRing<?> semiring(ChunkDescription.SemiRing et) {
        return SemiRings.semirings.get(et);
    }

    private SemiRings() {
    }
}
