package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.NodeAddress;
import it.unipr.aotlab.dmat.core.net.messages.MessagePrepareForMulticast;
import it.unipr.aotlab.dmat.core.net.messages.MessageUtils;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;
import it.unipr.aotlab.dmat.core.util.Assertion;
import it.unipr.aotlab.dmat.core.workingnode.NodeInfo;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class MessageSender implements
        it.unipr.aotlab.dmat.core.net.MessageSender {
    public String nodeId;
    public ZMQ.Context zmqContext;

    public Map<String, NodeAddress> nodeWorkGroup;
    public String broadcastAddress;
    public String broadcastPort;
    public String syncPort;

    public MessageSender(ZMQ.Context zmqContext,
            String nodeId,
            Address broadcastAddress,
            String syncPort) {
        /* for the working node */
        this.nodeWorkGroup = null;
        this.nodeId = nodeId;
        this.zmqContext = zmqContext;
        this.broadcastAddress = broadcastAddress.getHost();
        this.broadcastPort = Integer.toString(broadcastAddress.getPort());
        this.syncPort = syncPort;
    }

    public MessageSender(NodeWorkGroup nodeWorkGroup,
            String nodeId,
            Address broadcastAddress,
            String syncPort) {
        /* for the master node */
        this.nodeWorkGroup = nodeWorkGroup.nodesMap();
        this.nodeId = nodeId;
        this.zmqContext = nodeWorkGroup.getSocketContext();
        this.broadcastAddress = broadcastAddress.getHost();
        this.broadcastPort = Integer.toString(broadcastAddress.getPort());
        this.syncPort = syncPort;

        this.nodeWorkGroup.put(this.nodeId,
                new NodeInfo(this.nodeId, nodeWorkGroup.masterAddress().getHost(),  nodeWorkGroup.masterAddress().getPort()));
    }

    @Override
    public void sendMessage(Message m, String nodeName) throws IOException,
                                                               NodeNotFound {
        sendMessage(m, nodeWorkGroup.get(nodeName).getAddress());
    }

    public void sendMessage(Message m, Address ipAddress) {
        String address = ipAddress.getHost();
        int port = ipAddress.getPort();
        EnvelopedMessageBody envelopedMessage = MessageUtils.putInEnvelope(m);

        ZMQ.Socket socket = zmqContext.socket(ZMQ.REQ);

        try {
            System.err.println("XXX connecting tcp://" + address + ":" + port);
            System.err.println("XXX sending serialno " + m.serialNo());
            socket.connect("tcp://" + address + ":" + port);

            socket.send(envelopedMessage.toByteArray(), 0);
            socket.recv(0);
        }
        finally {
            socket.close();
        }
    }

    @Override
    public void multicastMessage(Message m, Collection<String> recipientsList)
            throws IOException, NodeNotFound {
        if (recipientsList.size() == 0)
            return;

        if (recipientsList.size() == 1) {
            Iterator<String> nodes = recipientsList.iterator();
            sendMessage(m, nodes.next());

            Assertion.isFalse(nodes.hasNext(),
                                  "Sent in unicast a message with"
                                          + " multiple recipients?!?");
            return;
        }

        multicastMessageImpl(m, recipientsList);
    }

    private void multicastMessageImpl(Message m, Collection<String> recipientsList) throws IOException, NodeNotFound {
        ZMQ.Socket syncService = null;

        ZMQ.Socket broadcast = zmqContext.socket(ZMQ.PUB);
        broadcast.bind("epgm://"
                + nodeWorkGroup.get(nodeId).getAddress().getHost()
                + ";" + broadcastAddress
                + ":" + broadcastPort);

        try {
            alertRecipients(recipientsList.iterator());
            try {
                syncService = zmqContext.socket(ZMQ.REP);
                syncService.setReceiveTimeOut(53);
                syncService.bind("tcp://*:" + syncPort);

                awaitSubscribers(broadcast, syncService, recipientsList.size());
            }
            finally {
                syncService.close();
            }

            actuallyBroadCastMessage(m, broadcast);
        }
        finally {
            broadcast.close();
        }
    }

    private void actuallyBroadCastMessage(Message m, Socket broadcast) {
        EnvelopedMessageBody envelopedMessage = MessageUtils.putInEnvelope(m);

        boolean b = broadcast.send(nodeId.getBytes(), ZMQ.SNDMORE);
        Assertion.isTrue(b, "broadcast socket is not sending!");

        b = broadcast.send(envelopedMessage.toByteArray(), 0);
        Assertion.isTrue(b, "broadcast socket is not sending!");
    }

    private void awaitSubscribers(Socket broadcast, Socket syncService, int size) {
        while (size > 0) {
            boolean b = broadcast.send(nodeId.getBytes(), ZMQ.SNDMORE);
            Assertion.isTrue(b, "broadcast socket is not sending!");

            b = broadcast.send("".getBytes(), 0);
            Assertion.isTrue(b, "broadcast socket is not sending!");

            byte[] readySignal = syncService.recv(0);
            if (readySignal != null) {
                syncService.send("".getBytes(), 0);
                --size;
            }
        }

        boolean b = broadcast.send(nodeId.getBytes(), ZMQ.SNDMORE);
        Assertion.isTrue(b, "broadcast socket is not sending!");

        b = broadcast.send("R".getBytes(), 0);
        Assertion.isTrue(b, "broadcast socket is not sending!");
    }

    private void alertRecipients(Iterator<String> nodes) throws IOException, NodeNotFound {
        while (nodes.hasNext()) {
            String node = nodes.next();
            sendMessage(new MessagePrepareForMulticast(nodeId), node);
        }
    }

    @Override
    public void meetTheWorkGroup(Map<String, NodeAddress> workgroup) {
        this.nodeWorkGroup = workgroup;
    }
}
