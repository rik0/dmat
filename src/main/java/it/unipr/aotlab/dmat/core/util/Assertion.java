package it.unipr.aotlab.dmat.core.util;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;

public class Assertion {
    private Assertion() {}

    public static void isTrue(Boolean condition, String message) {
        if (!condition) throw new DMatInternalError(message);
    }
    
    public static void isFalse(Boolean condition, String message) {
        isTrue(!condition, message);
    }
}
