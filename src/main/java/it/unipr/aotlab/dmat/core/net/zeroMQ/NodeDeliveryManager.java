package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.util.Utils;
import it.unipr.aotlab.dmat.core.workingnode.WorkingNode;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.zeromq.ZMQ.Context;

public class NodeDeliveryManager extends it.unipr.aotlab.dmat.core.net.NodeDeliveryManager {
    MessageReader messageReader;
    WorkingNode workingNode;

    LinkedList<Thread> threads = new LinkedList<Thread>();

    public NodeDeliveryManager(WorkingNode workingNode, String port,
            Context context) {
        super(workingNode.state);
        this.messageReader = new MessageReader(context, port);
        this.workingNode = workingNode;
    }

    @Override
    public void close() throws IOException {
        messageReader.stop();
        awaitThreads();
    }

    @Override
    public void initialize() throws IOException {
        Thread messageReaderThread = new Thread(messageReader);
        messageReaderThread.start();

        threads.push(messageReaderThread);
    }

    @Override
    protected EnvelopedMessageBody awaitNextDelivery() throws Exception {
        return messageReader.awaitNextDelivery();
    }

    public void prepareForReceivingMulticast() {
        clearThreadsList();

        BroadcastReader reader = new BroadcastReader((MessageSender)workingNode.messageSender,
                            workingNode.nodeId);
        Thread readerThread = new Thread(reader);
        readerThread.start();

        threads.push(readerThread);
    }

    private void clearThreadsList() {
        Iterator<Thread> i = threads.iterator();
        while (i.hasNext()) {
            Thread t = i.next();

            if (t.getState() == Thread.State.TERMINATED) {
                Utils.awaitThreadDeath(t);
                i.remove();
            }
        }
    }

    private void awaitThreads() {
        Iterator<Thread> i = threads.iterator();
        while (i.hasNext()) {
            Thread t = i.next();
            Utils.awaitThreadDeath(t);
            i.remove();
        }
    }
}
