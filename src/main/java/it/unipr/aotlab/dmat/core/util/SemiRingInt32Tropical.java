package it.unipr.aotlab.dmat.core.util;

public class SemiRingInt32Tropical implements SemiRing<Integer> {
    Integer INFINITY = Integer.MAX_VALUE;
    static SemiRingInt32Tropical theOne = null;

    @Override
    public SemiRing<Integer> get() {
        if (theOne == null)
            theOne = new SemiRingInt32Tropical();
        return theOne;
    }

    @Override
    public Integer add(Integer firstAddendum, final Integer secondAddendum) {
        return Math.min(firstAddendum, secondAddendum);
    }

    @Override
    public Integer times(Integer multiplicand, final Integer multiplier) {
        return multiplicand + multiplier;
    }

    @Override
    public Integer zero() {
        return INFINITY;
    }

    @Override
    public Integer one() {
        return 0;
    }

    private SemiRingInt32Tropical() {
    }
}
