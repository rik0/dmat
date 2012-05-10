package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.util.Utils;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;
import java.util.LinkedList;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;

public class NodeDeliveryManager extends it.unipr.aotlab.dmat.core.net.NodeDeliveryManager {
    ZMQ.Context context;
    String port;

    MessageReader messageReader;
    Thread messageReaderThread;

    LinkedList<EnvelopedMessageBody> messages = new LinkedList<EnvelopedMessageBody>();

    public synchronized void pushback(EnvelopedMessageBody m) {
        messages.addLast(m);
    }

    public synchronized EnvelopedMessageBody pullfront() {
        return messages.pollFirst();
    }

    public class MessageReader implements Runnable {
        boolean work = true;;
        ZMQ.Socket messageGetter;

        public synchronized void stop() {
            work = false;
        }

        public synchronized boolean awaitingMessages() {
            return work;
        }

        public void initialize() throws IOException {
            this.messageGetter = context.socket(ZMQ.REP);
            this.messageGetter.setReceiveTimeOut(251);

            System.err.println("XXX binding + tcp://*:" + port);
            this.messageGetter.bind("tcp://*:" + port);
        }

        @Override
        public void run() {
            EnvelopedMessageBody m;
            byte[] rawMessage;

            try {
                while (awaitingMessages()) {
                    rawMessage = messageGetter.recv(0);
                    if (rawMessage != null) {
                        m = EnvelopedMessageBody.parseFrom(rawMessage);
                        messageGetter.send("".getBytes(), 0);
                        pushback(m);
                    }
                }
                messageGetter.close();
            } catch (InvalidProtocolBufferException e) {
                throw new DMatInternalError("Exception from "
                        + e.getClass().getCanonicalName()
                        + " ." + e.getMessage());
            }
        }
    }

    public NodeDeliveryManager(NodeState currentState,
                               String port,
                               ZMQ.Context context) {
        super(currentState);
        this.port = port;
        this.context = context;
        this.messageReader = new MessageReader();
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
        EnvelopedMessageBody m = pullfront();
        while (m == null) {
            Utils.sleep(499);
            m = pullfront();
        }

        return m;
    }
}
