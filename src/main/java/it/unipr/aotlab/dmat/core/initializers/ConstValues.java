/* Copyright */

package it.unipr.aotlab.dmat.core.initializers;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.initializers
 * Date: 10/18/11
 * Time: 10:58 AM
 */
public class ConstValues<E extends Number> implements Initializer {
    private E value;

    public ConstValues(E value) {
        this.value = value;
    }

    public E getValue() {
        return value;
    }
}
