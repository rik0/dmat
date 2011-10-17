/* Copyright */


package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.PartitionError;
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

// TODO: what do we mean with multiple matrix factories?
// TODO: how to allocate nodes?
abstract public class MatrixFactory {
    abstract Configurator getConfigurator();
    abstract protected Matrix createMatrix (
            Type type,
            Partition p,
            Shape s,
            Format f
    ) throws PartitionError;

    /* createSparse(), etc */

}
