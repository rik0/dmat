package it.unipr.aotlab.dmat.core.initializers;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;

import java.util.EnumMap;

public class Initializers {
    static EnumMap<ChunkDescription.ElementType, Initializer> defaultInitializers = new EnumMap<ChunkDescription.ElementType, Initializer>(
            ChunkDescription.ElementType.class);

    static void addDefaultInitializer(ChunkDescription.ElementType type,
            final Initializer init) {
        defaultInitializers.put(type, init);
    }

    static public Initializer defaultInitializer(
            ChunkDescription.ElementType type) {
        return defaultInitializers.get(type);
    }
}
