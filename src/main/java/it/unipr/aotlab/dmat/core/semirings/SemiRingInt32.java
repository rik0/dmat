package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.util.ElementType;

public class SemiRingInt32 implements SemiRing<Integer> {
    static SemiRingInt32 theOne = new SemiRingInt32();
    static {
        SemiRings.addDefaultSemiring(ElementType.INT32, theOne);
        SemiRings.addDefaultSemiring(ElementType.UINT32, theOne);
    }

    static public SemiRing<Integer> get() {
        return theOne;
    }

    @Override
    public Integer add(Integer firstAddendum, final Integer secondAddendum) {
        return firstAddendum + secondAddendum;
    }

    @Override
    public Integer times(Integer multiplicand, final Integer multiplier) {
        return multiplicand * multiplier;
    }

    @Override
    public Integer zero() {
        return 0;
    }

    @Override
    public Integer one() {
        return 1;
    }

    private SemiRingInt32() {
    }
}
