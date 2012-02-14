package it.unipr.aotlab.dmat.core.initializers;

import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;

import java.util.EnumMap;

public class Initializers {
    static EnumMap<ChunkDescriptionWire.ElementType, Initializer> defaultInitializers = new EnumMap<ChunkDescriptionWire.ElementType, Initializer>(
            ChunkDescriptionWire.ElementType.class);

    static void addDefaultInitializer(ChunkDescriptionWire.ElementType type,
            final Initializer init) {
        defaultInitializers.put(type, init);
    }

    static public Initializer defaultInitializer(
            ChunkDescriptionWire.ElementType type) {
        return defaultInitializers.get(type);
    }
}
