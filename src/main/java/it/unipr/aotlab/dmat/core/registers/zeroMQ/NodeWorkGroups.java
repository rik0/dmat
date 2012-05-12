package it.unipr.aotlab.dmat.core.registers.zeroMQ;

import it.unipr.aotlab.dmat.core.errors.InvalidNode;
import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.zeroMQ.MessageSender;

import org.zeromq.ZMQ;

public class NodeWorkGroups {
    int nofZMQContextTheads = -1;
    NodeWorkGroup buildingObject = new NodeWorkGroup();
    Address broadcastAddress;
    String syncPort;

    NodeWorkGroups() {}

    public NodeWorkGroups masterId(String masterId) {
        buildingObject.masterId = masterId;
        return this;
    }

    public NodeWorkGroups masterAddress(Address masterAddress) {
        buildingObject.masterAddress = masterAddress;
        return this;
    }

    public NodeWorkGroups broadcastAddress(Address broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
        return this;
    }

    public NodeWorkGroups syncPort(String syncPort) {
        this.syncPort = syncPort;
        return this;
    }

    public NodeWorkGroups nofZmqContextThreads(int nofThreads) {
        this.nofZMQContextTheads = nofThreads;
        return this;
    }

    public void invalidateFactory() {
        buildingObject = null;
    }

    public NodeWorkGroup build() {
        NodeWorkGroup objectBuilt = buildingObject;

        validateBuildingObject();
        invalidateFactory();

        return objectBuilt;
    }

    private void validateBuildingObject() {
        if (buildingObject.masterId == null)
            throw new InvalidNode("No master ID!");

        if (buildingObject.masterAddress == null)
            throw new InvalidNode("No master address!");

        if (nofZMQContextTheads < 1)
            nofZMQContextTheads = 1;

        if (broadcastAddress == null)
            broadcastAddress = new IPAddress("239.255.255.255", 43981);

        if  (syncPort == null)
            syncPort = "43982";

        buildingObject.zmqContext = ZMQ.context(nofZMQContextTheads);
        buildingObject.messageSender = new MessageSender(
                buildingObject,
                buildingObject.masterId,
                buildingObject.masterAddress.getHost(),
                broadcastAddress,
                syncPort);
    }
}
