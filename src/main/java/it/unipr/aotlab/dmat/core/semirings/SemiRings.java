package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Messages;
import it.unipr.aotlab.dmat.core.util.ForceLoad;

import java.util.EnumMap;

public class SemiRings {
    static EnumMap<TypeWire.ElementType, SemiRing> defaultSemirings = new EnumMap<TypeWire.ElementType, SemiRing>(
            TypeWire.ElementType.class);

    static EnumMap<TypeWire.SemiRing, SemiRing> semirings = new EnumMap<TypeWire.SemiRing, SemiRing>(
            TypeWire.SemiRing.class);

    static {
        ForceLoad.listFromFile(Messages.class, "KindOfSemiRings");
    }

    static public SemiRing defaultSemiring(TypeWire.ElementType et) {
        return SemiRings.defaultSemirings.get(et);
    }

    static public SemiRing semiring(TypeWire.SemiRing et) {
        return SemiRings.semirings.get(et);
    }

    static public SemiRing semiring(TypeWire.TypeBody type) {
        TypeWire.SemiRing sr = type.getSemiRing();
        if (sr.compareTo(TypeWire.SemiRing.DEFAULTSEMIRING) == 0)
            return SemiRings.defaultSemiring(type.getElementType());

        return SemiRings.semiring(sr);
    }

    private SemiRings() {
    }
}
