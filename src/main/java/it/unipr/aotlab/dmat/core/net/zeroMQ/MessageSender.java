package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.errors.NodeNotFound;
import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Address;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.NodeAddress;
import it.unipr.aotlab.dmat.core.net.messages.MessageUtils;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.zeromq.ZMQ;

public class MessageSender implements
        it.unipr.aotlab.dmat.core.net.MessageSender {
    Map<String, NodeAddress> nodeWorkGroup;
    ZMQ.Context zmqContext;

    public MessageSender(ZMQ.Context zmqContext) {
        /* for the working node */
        this.nodeWorkGroup = null;
        this.zmqContext = zmqContext;
    }

    public MessageSender(NodeWorkGroup nodeWorkGroup) {
        /* for the master node */
        this.nodeWorkGroup = nodeWorkGroup.nodesMap();
        this.zmqContext = nodeWorkGroup.getSocketContext();
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
    public void multicastMessage(Message m, Iterable<String> list)
            throws IOException, NodeNotFound {

        // XXX it will be replaced by true multicasting!
        Iterator<String> nodes = list.iterator();
        while (nodes.hasNext()) {
            String node = nodes.next();

            sendMessage(m, node);
        }
    }

    @Override
    public void meetTheWorkGroup(Map<String, NodeAddress> workgroup) {
        this.nodeWorkGroup = workgroup;
    }
}
