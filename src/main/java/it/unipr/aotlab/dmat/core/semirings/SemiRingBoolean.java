package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.TypeWire;

public class SemiRingBoolean implements SemiRing<Boolean> {
    static TypeWire.SemiRing srtag = TypeWire.SemiRing.BOOLEANORDINARY;
    static SemiRingBoolean theOne = new SemiRingBoolean();
    static {
        SemiRings.defaultSemirings.put(TypeWire.ElementType.BOOL, theOne);
        SemiRings.semirings.put(srtag, theOne);
    }

    public static SemiRing<Boolean> get() {
        return theOne;
    }

    @Override
    public Boolean add(Boolean firstAddendum, final Boolean secondAddendum) {
        return firstAddendum || secondAddendum;
    }

    @Override
    public Boolean times(Boolean multiplicand, final Boolean multiplier) {
        return multiplicand && multiplier;
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
