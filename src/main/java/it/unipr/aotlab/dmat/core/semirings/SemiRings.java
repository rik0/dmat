package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Messages;
import it.unipr.aotlab.dmat.core.util.ForceLoad;

import java.util.EnumMap;

public class SemiRings {
    static EnumMap<ChunkDescriptionWire.ElementType, SemiRing<?>> defaultSemirings = new EnumMap<ChunkDescriptionWire.ElementType, SemiRing<?>>(
            ChunkDescriptionWire.ElementType.class);

    static EnumMap<ChunkDescriptionWire.SemiRing, SemiRing<?>> semirings = new EnumMap<ChunkDescriptionWire.SemiRing, SemiRing<?>>(
            ChunkDescriptionWire.SemiRing.class);

    static {
        ForceLoad.listFromFile(Messages.class, "KindOfSemiRings");
    }

    static public SemiRing<?> defaultSemiring(ChunkDescriptionWire.ElementType et) {
        return SemiRings.defaultSemirings.get(et);
    }

    static public SemiRing<?> semiring(ChunkDescriptionWire.SemiRing et) {
        return SemiRings.semirings.get(et);
    }

    private SemiRings() {
    }
}
