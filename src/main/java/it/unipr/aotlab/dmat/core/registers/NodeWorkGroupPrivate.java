package it.unipr.aotlab.dmat.core.registers;

import it.unipr.aotlab.dmat.core.net.Node;

public interface NodeWorkGroupPrivate {
    void registerNode(Node node) throws Exception;

    int getNextOrderId();
}
