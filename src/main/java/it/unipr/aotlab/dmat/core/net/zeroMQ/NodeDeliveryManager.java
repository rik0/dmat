package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;

import org.zeromq.ZMQ;

public class NodeDeliveryManager extends it.unipr.aotlab.dmat.core.net.NodeDeliveryManager {
    ZMQ.Context context;
    String port;
    ZMQ.Socket messageGetter;

    public NodeDeliveryManager(NodeState currentState, String port) {
        super(currentState);
        this.port = port;
    }

    @Override
    public void close() throws IOException {
        messageGetter.close();
    }

    @Override
    public void initialize() throws IOException {
        this.messageGetter = context.socket(ZMQ.REQ);
        this.messageGetter.bind("tcp://*:" + port);
    }

    @Override
    protected EnvelopedMessageBody awaitNextDelivery() throws Exception {
        return EnvelopedMessageBody
                .parseFrom(messageGetter.recv(0));
    }
}
