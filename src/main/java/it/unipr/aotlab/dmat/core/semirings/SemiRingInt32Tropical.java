package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.TypeWire;

public class SemiRingInt32Tropical implements SemiRing {
    static final private TypeWire.SemiRing srtag = TypeWire.SemiRing.INT32TROPICAL;
    static final private Integer INFINITY = Integer.MAX_VALUE;
    static SemiRingInt32Tropical theOne = null;

    static {
        SemiRings.semirings.put(srtag, theOne);
    }

    public static SemiRing get() {
        if (theOne == null)
            theOne = new SemiRingInt32Tropical();

        return theOne;
    }

    @Override
    public Integer add(Object firstAddendum,  Object secondAddendum) {
        return Math.min((Integer)firstAddendum, (Integer)secondAddendum);
    }

    @Override
    public Integer times(Object multiplicand, Object multiplier) {
        Integer tmultiplicand = (Integer)multiplicand;
        Integer tmultiplier = (Integer)multiplier;

        if (tmultiplicand > INFINITY - tmultiplier)
            return INFINITY;

        return tmultiplicand + tmultiplier;
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
