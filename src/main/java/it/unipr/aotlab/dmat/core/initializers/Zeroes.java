/* Copyright */

package it.unipr.aotlab.dmat.core.initializers;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.initializers
 * Date: 10/18/11
 * Time: 10:57 AM
 */

/**
 * Sets all the
 */
public class Zeroes<E extends Number> extends ConstValues<E> {

    public Zeroes() {
        super((E) 0);
    }
}
