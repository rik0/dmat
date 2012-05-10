package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.util.Utils;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;

import org.zeromq.ZMQ;

public class NodeDeliveryManager extends it.unipr.aotlab.dmat.core.net.NodeDeliveryManager {
    MessageReader messageReader;
    Thread messageReaderThread;

    public NodeDeliveryManager(NodeState currentState,
                               String port,
                               ZMQ.Context context) {
        super(currentState);
        this.messageReader = new MessageReader(context, port);
    }

    @Override
    public void close() throws IOException {
        messageReader.stop();
        Utils.awaitThreadDeath(messageReaderThread);
    }

    @Override
    public void initialize() throws IOException {
        messageReader.initialize();
        this.messageReaderThread = new Thread(messageReader);
        this.messageReaderThread.start();
    }

    @Override
    protected EnvelopedMessageBody awaitNextDelivery() throws Exception {
        return messageReader.awaitNextDelivery();
    }
}
