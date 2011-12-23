package it.unipr.aotlab.dmat.core.util;

public class SemiRingInt32Tropical implements SemiRing<Integer> {
    final Integer INFINITY = Integer.MAX_VALUE;
    static SemiRingInt32Tropical theOne = null;

    @Override
    public SemiRing<Integer> get() {
        if (theOne == null)
            theOne = new SemiRingInt32Tropical();
        return theOne;
    }

    @Override
    public Integer add(final Integer firstAddendum, final Integer secondAddendum) {
        return Math.min(firstAddendum, secondAddendum);
    }

    @Override
    public Integer times(final Integer multiplicand, final Integer multiplier) {
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
