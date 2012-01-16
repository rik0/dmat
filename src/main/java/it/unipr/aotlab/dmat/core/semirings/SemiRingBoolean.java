package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;

public class SemiRingBoolean implements SemiRing<Boolean> {
    static SemiRingBoolean theOne = new SemiRingBoolean();
    static {
        SemiRings.defaultSemirings.put(ChunkDescription.ElementType.BOOL, theOne);
        SemiRings.semirings.put(ChunkDescription.SemiRing.BOOLEANORDINARY, theOne);
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
}
