package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.registers.NodeWorkGroup;

public class NodeInfo implements Node {
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

    @Override
    public Chunk getChunck(String id) throws ChunkNotFound {
        throw new DMatInternalError("NodeInfo is just a stub with address and port");
    }

    @Override
    public void addChunk(Chunk c) {
        throw new DMatInternalError("NodeInfo is just a stub with address and port");
    }

    @Override
    public boolean doesManage(String chunkId) {
        throw new DMatInternalError("NodeInfo is just a stub with address and port");
    }

    @Override
    public void sendMessage(Message m) throws Exception {
        throw new DMatInternalError("NodeInfo is just a stub with address and port");
    }

    @Override
    public NodeWorkGroup getWorkGroup() {
        throw new DMatInternalError("NodeInfo is just a stub with address and port");
    }
}
