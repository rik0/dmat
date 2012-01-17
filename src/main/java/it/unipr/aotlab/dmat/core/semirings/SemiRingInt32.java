package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;

public class SemiRingInt32 implements SemiRing<Integer> {
    static ChunkDescription.SemiRing srtag = ChunkDescription.SemiRing.INT32ORDINARY;
    static SemiRingInt32 theOne = new SemiRingInt32();

    static {
        SemiRings.defaultSemirings.put(ChunkDescription.ElementType.INT32,
                theOne);
        SemiRings.defaultSemirings.put(ChunkDescription.ElementType.UINT32,
                theOne);
        SemiRings.semirings.put(srtag, theOne);
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

    @Override
    public ChunkDescription.SemiRing valueOf() {
        return srtag;
    }
}
