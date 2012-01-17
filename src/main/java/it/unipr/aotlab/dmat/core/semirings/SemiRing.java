package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.generated.ChunkDescription;

public interface SemiRing<E> {
    public E add(E firstAddendum, E secondAddendum);

    public E times(E multiplicand, E multiplier);

    public E zero();

    public E one();
    
    public ChunkDescription.SemiRing valueOf();
}
