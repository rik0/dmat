package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.util.Assertion;

import org.zeromq.ZMQ;

public class BroadcastReader implements Runnable {
    ZMQ.Context context;
    ZMQ.Socket subscriber;
    ZMQ.Socket syncClient;
    ZMQ.Socket loopback;
    MessageSender messageSender;
    String senderId;
    private String recipientId;
    private String localPort;

    BroadcastReader(MessageSender messageSender,
                    String recipientId,
                    String senderId,
                    String localPort) {
        this.context = messageSender.zmqContext;
        this.recipientId = recipientId;
        this.messageSender = messageSender;
        this.senderId = senderId;
        this.localPort = localPort;
    }

    public void connectToSubscriberSocket() {
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);

        System.err.println("XXX + connecting to epgm://" + messageSender.broadcastAddress
                + ":" + messageSender.broadcastPort);
        System.err.println("XXX signature " + senderId);

        subscriber.connect("epgm://" + messageSender.broadcastAddress
                + ":" + messageSender.broadcastPort);
        subscriber.subscribe(senderId.getBytes());

        this.subscriber = subscriber;
    }

    public void connectToSyncService() {
        String address = messageSender.nodeWorkGroup.get(senderId).getAddress().getHost();
        String port = messageSender.syncPort;

        ZMQ.Socket syncClient = context.socket(ZMQ.REQ);
        syncClient.connect("tcp://" + address + ":" + port);

        this.syncClient = syncClient;
    }

    public void awaitPublisher() {
        byte[] p = subscriber.recv(0);
        Assertion.isTrue((new String(p)).equals(senderId), "Strange message arrived!");

        subscriber.recv(0);
    }

    public void syncWithPublisher() {
        syncClient.send(recipientId.getBytes(), 0);
        syncClient.recv(0);
    }

    public void awaitOtherSubscribers() {
        for (;;) {
            byte[] p = subscriber.recv(0);
            Assertion.isTrue((new String(p)).equals(senderId), "Strange message arrived!");

            byte[] pulseb = subscriber.recv(0);
            String pulse = new String(pulseb);

            if (pulse.equals("R"))
                break;
        }
    }

    public void connectInLoopBack() {
        ZMQ.Socket loopback = context.socket(ZMQ.REQ);
        loopback.connect("tcp://127.0.0.1:" + localPort);

        this.loopback = loopback;
    }

    public void getAndResendMessage() {
        byte[] p = subscriber.recv(0);
        Assertion.isTrue((new String(p)).equals(senderId), "Strange message arrived!");

        byte[] message = subscriber.recv(0);
        loopback.send(message, 0);
        loopback.recv(0);
    }

    @Override
    public void run() {
        try {
            connectToSubscriberSocket();
            connectToSyncService();
            awaitPublisher();
            syncWithPublisher();
            awaitOtherSubscribers();
            connectInLoopBack();
            getAndResendMessage();
        }
        finally {
            if (loopback != null)
                loopback.close();

            if (syncClient != null)
                syncClient.close();

            if (subscriber != null)
                subscriber.close();
        }
    }
}
