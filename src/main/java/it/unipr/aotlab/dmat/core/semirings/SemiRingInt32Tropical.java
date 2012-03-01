package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.TypeWire;

public class SemiRingInt32Tropical implements SemiRing<Integer> {
    static final private TypeWire.SemiRing srtag = TypeWire.SemiRing.INT32TROPICAL;
    static final private Integer INFINITY = Integer.MAX_VALUE;
    static SemiRingInt32Tropical theOne = null;

    static {
        SemiRings.semirings.put(srtag, theOne);
    }

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

    @Override
    public TypeWire.SemiRing valueOf() {
        return srtag;
    }
}
