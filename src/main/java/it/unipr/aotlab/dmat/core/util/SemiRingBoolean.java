package it.unipr.aotlab.dmat.core.util;

public class SemiRingBoolean implements SemiRing<Boolean> {
    static SemiRingBoolean theOne = new SemiRingBoolean();
    static {
        SemiRings.addDefaultSemiring(ElementType.BOOL, theOne);
    }

    @Override
    public SemiRing<Boolean> get() {
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
}
