package it.unipr.aotlab.dmat.core.initializers;

import it.unipr.aotlab.dmat.core.generated.TypeWire;

import java.util.EnumMap;

public class Initializers {
    static EnumMap<TypeWire.ElementType, Initializer> defaultInitializers = new EnumMap<TypeWire.ElementType, Initializer>(
            TypeWire.ElementType.class);

    static void addDefaultInitializer(TypeWire.ElementType type,
            final Initializer init) {
        defaultInitializers.put(type, init);
    }

    static public Initializer defaultInitializer(
            TypeWire.ElementType type) {
        return defaultInitializers.get(type);
    }
}
