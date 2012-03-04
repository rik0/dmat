package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.TypeWire;

public class SemiRingInt32 implements SemiRing {
    static TypeWire.SemiRing srtag = TypeWire.SemiRing.INT32ORDINARY;
    static SemiRingInt32 theOne = new SemiRingInt32();

    static {
        SemiRings.defaultSemirings.put(TypeWire.ElementType.INT32,
                theOne);
        SemiRings.defaultSemirings.put(TypeWire.ElementType.UINT32,
                theOne);
        SemiRings.semirings.put(srtag, theOne);
    }

    static public SemiRing get() {
        return theOne;
    }

    @Override
    public Integer add(Object firstAddendum, Object secondAddendum) {
        return (Integer)firstAddendum + (Integer)secondAddendum;
    }

    @Override
    public Integer times(Object multiplicand, Object multiplier) {
        return (Integer)multiplicand * (Integer)multiplier;
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

    @Override
    public TypeWire.SemiRing valueOf() {
        return srtag;
    }
}
