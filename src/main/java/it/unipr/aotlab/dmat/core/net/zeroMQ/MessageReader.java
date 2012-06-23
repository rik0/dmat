package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageImmediate;
import it.unipr.aotlab.dmat.core.net.NodeDeliveryManager;
import it.unipr.aotlab.dmat.core.net.messages.Messages;
import it.unipr.aotlab.dmat.core.util.Utils;

import java.io.IOException;
import java.util.LinkedList;

import org.zeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;

public class MessageReader implements Runnable {
    public NodeDeliveryManager nodeDeliveryManager;
    public ZMQ.Context context;
    public ZMQ.Socket messageGetter;
    public boolean work = true;
    public String port;
    public LinkedList<EnvelopedMessageBody> messages = new LinkedList<EnvelopedMessageBody>();

    public MessageReader(NodeDeliveryManager nodeDeliveryManager, ZMQ.Context context, String port) {
        this.nodeDeliveryManager = nodeDeliveryManager;
        this.port = port;
        this.context = context;
    }

    public MessageReader(ZMQ.Context context, String port) {
        this.nodeDeliveryManager = null;
        this.port = port;
        this.context = context;
    }

    public synchronized void pushback(EnvelopedMessageBody m) {
        messages.addLast(m);
    }

    public synchronized EnvelopedMessageBody pullfront() {
        return messages.pollFirst();
    }

    public synchronized void stop() {
        work = false;

    }

    public synchronized void manage(EnvelopedMessageBody m)
            throws InvalidProtocolBufferException {
        if (m.getMessageKind() == Message.MessageKind.IMMEDIATE.tag) {
            MessageImmediate immediate = (MessageImmediate)Messages.readMessage(m);
            immediate.immediateAction(this, m);
        }
        else {
            messages.addLast(m);
        }
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

    public EnvelopedMessageBody awaitNextDelivery() throws Exception {
        EnvelopedMessageBody m = pullfront();
        while (m == null) {
            Utils.sleep(109);
            m = pullfront();
        }

        return m;
    }

    @Override
    public void run() {
        try {
            initialize();
        } catch (IOException e1) {
            throw new DMatInternalError(e1 + ":" + e1.getMessage());
        }

        EnvelopedMessageBody m;
        byte[] rawMessage;

        try {
            while (awaitingMessages()) {
                rawMessage = messageGetter.recv(0);
                if (rawMessage != null) {
                    m = EnvelopedMessageBody.parseFrom(rawMessage);
                    messageGetter.send("".getBytes(), 0);
                    manage(m);
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
