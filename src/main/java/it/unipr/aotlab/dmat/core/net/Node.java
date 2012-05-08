package it.unipr.aotlab.dmat.core.net;

import java.util.Comparator;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.net
 * Date: 10/18/11
 * Time: 11:06 AM
 */
public interface Node extends NodeAddress, NodeData {
    static class NodeComparor implements Comparator<Node>{
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getNodeId().compareTo(o2.getNodeId());
        }
    }
}
