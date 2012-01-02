package it.unipr.aotlab.dmat.core.semirings;

public class SemiRingInt32Tropical implements SemiRing<Integer> {
    Integer INFINITY = Integer.MAX_VALUE;
    static SemiRingInt32Tropical theOne = null;

    public static SemiRing<Integer> get() {
        if (theOne == null)
            theOne = new SemiRingInt32Tropical();
        return theOne;
    }

    @Override
    public Integer add(Integer firstAddendum, Integer secondAddendum) {
        return Math.min(firstAddendum, secondAddendum);
    }

    @Override
    public Integer times(Integer multiplicand, Integer multiplier) {
        if (multiplicand > INFINITY - multiplier)
            return INFINITY;

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
