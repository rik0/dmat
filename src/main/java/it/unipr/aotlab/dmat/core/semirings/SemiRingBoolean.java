package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.TypeWire;

public class SemiRingBoolean implements SemiRing {
    static TypeWire.SemiRing srtag = TypeWire.SemiRing.BOOLEANORDINARY;
    static SemiRingBoolean theOne = new SemiRingBoolean();
    static {
        SemiRings.defaultSemirings.put(TypeWire.ElementType.BOOL, theOne);
        SemiRings.semirings.put(srtag, theOne);
    }

    public static SemiRing get() {
        return theOne;
    }

    @Override
    public Boolean add(Object firstAddendum, Object secondAddendum) {
        return (Boolean)firstAddendum || (Boolean)secondAddendum;
    }

    @Override
    public Boolean times(Object multiplicand, Object multiplier) {
        return (Boolean)multiplicand && (Boolean)multiplier;
    }

    @Override
    public Boolean zero() {
        return false;
    }

    @Override
    public Boolean one() {
        return true;
    }

    private SemiRingBoolean() {
    }

    @Override
    public TypeWire.SemiRing valueOf() {
        return srtag;
    }
}
