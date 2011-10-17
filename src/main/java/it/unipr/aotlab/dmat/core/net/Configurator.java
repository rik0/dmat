package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.net.Login;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core
 * Date: 10/17/11
 * Time: 2:08 PM
 */
public interface Configurator {
    void addNode(Address a);
    void addNode(Address a, Login l);
}
