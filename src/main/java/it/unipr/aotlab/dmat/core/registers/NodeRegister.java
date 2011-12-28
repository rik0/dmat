package it.unipr.aotlab.dmat.core.registers;

import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.net.Node;

import java.util.LinkedHashMap;
import java.util.Map;

public class NodeRegister {
    Map<String, Node> nodes = new LinkedHashMap<String, Node>();

    public void registerNode(final String id, final Node n)
            throws IdNotUnique {
        if (nodes.get(id) != null)
            throw new IdNotUnique();

        nodes.put(id, n);
    }

    public Node getNode(final String id) throws NodeNotFound {
        final Node n = nodes.get(id);
        if (n == null)
            throw new NodeNotFound();

        return n;
    }
}
