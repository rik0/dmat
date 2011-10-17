package it.unipr.aotlab.dmat.core.shapes;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.shapes
 * Date: 10/17/11
 * Time: 2:38 PM
 */
public interface Shape {
    /* These methods are examples */
    boolean isSparse();
    boolean hasDiagonal();
    boolean hasUpperTriangular();
    boolean hasLowerTriangular();
    boolean isSymmetric();
}
