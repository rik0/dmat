package it.unipr.aotlab.dmat.core.net;

import java.util.Vector;

public class ConfiguratorImpl implements Configurator {
    private Vector<Node> nodes = new Vector<Node>();

    @Override
    public void addNode(Node n) {
        // n.setCluster(nodes);
        nodes.add(n);
    }
}
