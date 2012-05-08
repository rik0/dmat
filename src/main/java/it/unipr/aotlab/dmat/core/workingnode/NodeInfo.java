package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.NodeAddress;

public class NodeInfo implements NodeAddress {
    String nodeId;
    IPAddress address;

    public NodeInfo(String nodeId, String host, int port) {
        super();
        this.nodeId = nodeId;
        this.address = new IPAddress(host, port);
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
