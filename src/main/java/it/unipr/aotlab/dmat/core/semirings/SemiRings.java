package it.unipr.aotlab.dmat.core.semirings;

import it.unipr.aotlab.dmat.core.util.ElementType;
import it.unipr.aotlab.dmat.core.util.PackageGetClasses;

import java.util.EnumMap;

public class SemiRings {
    static EnumMap<ElementType, SemiRing<?>> defaultSemirings = new EnumMap<ElementType, SemiRing<?>>(
            ElementType.class);

    static {
        PackageGetClasses.execOnClasses("it.unipr.aotlab.dmat.core.semirings",
                new PackageGetClasses.ClassNameFilter() {
                    @Override
                    public boolean accept(String className) {
                        return true;
                    }
                }, new PackageGetClasses.OnClassExecutor() {
                    @Override
                    public void execOnClass(Class<?> klass) {
                    }
                });
    }

    static void addDefaultSemiring(ElementType et, SemiRing<?> sr) {
        defaultSemirings.put(et, sr);
    }

    static public SemiRing<?> defaultSemiring(ElementType et) {
        return SemiRings.defaultSemirings.get(et);
    }
    
    private SemiRings() {}
}
