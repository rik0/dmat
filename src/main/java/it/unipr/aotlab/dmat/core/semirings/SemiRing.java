package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.TypeWire;

public interface SemiRing {
    public Object add(Object firstAddendum, Object secondAddendum);

    public Object times(Object multiplicand, Object multiplier);

    public Object zero();

    public Object one();
    
    public TypeWire.SemiRing valueOf();
}
