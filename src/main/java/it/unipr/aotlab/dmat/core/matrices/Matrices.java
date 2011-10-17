/* Copyright */


package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.net.Configurator;
import it.unipr.aotlab.dmat.core.formats.Format;
import it.unipr.aotlab.dmat.core.partitions.Partition;
import it.unipr.aotlab.dmat.core.shapes.Shape;
import it.unipr.aotlab.dmat.core.util.Type;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.matrices
 * Date: 10/17/11
 * Time: 2:52 PM
 */
abstract public class Matrices {
    abstract Configurator getConfigurator();
    abstract protected Matrix createMatrix(
            int cols,
            int rows,
            Type type,
            Partition p,
            Shape s,
            Format f
    );

    /* createSparse(), etc */

}
