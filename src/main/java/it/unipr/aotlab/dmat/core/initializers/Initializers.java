package it.unipr.aotlab.dmat.core.initializers;

import it.unipr.aotlab.dmat.core.util.ElementType;

import java.util.EnumMap;

public class Initializers {
    static EnumMap<ElementType, Initializer> defaultInitializers = new EnumMap<ElementType, Initializer>(
            ElementType.class);

    static void addDefaultInitializer(final ElementType type, final Initializer init) {
        defaultInitializers.put(type, init);
    }

    static public Initializer defaultInitializer(final ElementType type) {
        return defaultInitializer(type);
    }
}
