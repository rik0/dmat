package it.unipr.aotlab.test.dmat;

import static junit.framework.Assert.assertEquals;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.semirings.SemiRing;
import it.unipr.aotlab.dmat.core.semirings.SemiRingInt32Tropical;
import it.unipr.aotlab.dmat.core.semirings.SemiRings;

import org.junit.Test;

public class SemiRingsTestCase {
    public static class Values<E> {
        E a;
        E b;
        E c;
    }

    @Test
    public void int32DefaultSemiRing() {
        @SuppressWarnings("unchecked")
        SemiRing<Integer> sr = ((SemiRing<Integer>) SemiRings
                .defaultSemiring(TypeWire.ElementType.INT32));
        Values<Integer> v = new Values<Integer>();
        v.a = 7;
        v.b = 11;
        v.c = 13;
        intSemiRingProps(v, sr);
        intSemiRingOps(v, sr, 18, 77);
    }

    @Test
    public void int32TropicalSemiRing() {
        SemiRing<Integer> sr = SemiRingInt32Tropical.get();
        Values<Integer> v = new Values<Integer>();
        v.a = 7;
        v.b = 11;
        v.c = 13;
        intSemiRingProps(v, sr);
        intSemiRingOps(v, sr, 7, 18);
    }

    @Test
    public void booleanDefaultSemiRing() {
        @SuppressWarnings("unchecked")
        SemiRing<Boolean> sr = (SemiRing<Boolean>) SemiRings
                .defaultSemiring(TypeWire.ElementType.BOOL);
        Values<Boolean> v = new Values<Boolean>();
        v.a = true;
        v.b = false;
        v.c = true;

        intSemiRingProps(v, sr);
        intSemiRingOps(v, sr, true, false);
    }

    private static <E> void intSemiRingProps(Values<E> v, SemiRing<E> sr) {
        plusFormsCommutativeMonoid(v, sr);
        timesFormsMonoidWithId(v, sr);
        timesDistributesPlus(v, sr);
        zeroAnnihilatesTimes(v, sr);
    }

    public static <E> void plusFormsCommutativeMonoid(Values<E> v,
            SemiRing<E> sr) {
        E d, dd;
        d = sr.add(v.a, v.b);
        d = sr.add(d, v.c);
        dd = sr.add(v.b, v.c);
        dd = sr.add(v.a, dd);
        assertEquals(d, dd);

        d = sr.add(v.a, sr.zero());
        dd = sr.add(sr.zero(), v.a);
        assertEquals(d, dd);
        assertEquals(d, v.a);

        d = sr.add(v.a, v.b);
        dd = sr.add(v.b, v.a);
        assertEquals(d, dd);
    }

    public static <E> void timesFormsMonoidWithId(Values<E> v, SemiRing<E> sr) {
        E d, dd;
        d = sr.times(v.a, v.b);
        d = sr.times(d, v.c);

        dd = sr.times(v.b, v.c);
        dd = sr.times(v.a, dd);
        assertEquals(dd, d);

        d = sr.times(v.a, sr.one());
        dd = sr.times(sr.one(), v.a);
        assertEquals(dd, d);
    }

    public static <E> void timesDistributesPlus(Values<E> v, SemiRing<E> sr) {
        E d, dd, t;

        d = sr.add(v.b, v.c);
        d = sr.times(v.a, d);

        dd = sr.times(v.a, v.b);
        t = sr.times(v.a, v.c);
        dd = sr.add(dd, t);
        assertEquals(dd, d);

        d = sr.add(v.a, v.b);
        d = sr.times(d, v.c);

        dd = sr.times(v.a, v.c);
        t = sr.times(v.b, v.c);
        dd = sr.add(dd, t);
        assertEquals(dd, d);

    }

    public static <E> void zeroAnnihilatesTimes(Values<E> v, SemiRing<E> sr) {
        E d, dd;

        d = sr.times(v.a, sr.zero());
        dd = sr.times(sr.zero(), v.a);

        assertEquals(dd, d);
        assertEquals(dd, sr.zero());
    }

    public static <E> void intSemiRingOps(Values<E> v, SemiRing<E> sr,
            E sumExp, E mulExp) {
        assertEquals(sumExp, sr.add(v.a, v.b));
        assertEquals(mulExp, sr.times(v.a, v.b));
    }
}
