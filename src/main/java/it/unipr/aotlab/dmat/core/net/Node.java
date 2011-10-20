package it.unipr.aotlab.dmat.core.net;

import java.util.Vector;

/**
 * User: enrico
 * Package: it.unipr.aotlab.dmat.core.net
 * Date: 10/18/11
 * Time: 11:06 AM
 */
public interface Node {
    public Vector<Node> getCluster();
    public void setCluster(Vector<Node> v);
    public void sendMessage(Message m);
}
