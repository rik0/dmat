package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.net.DeliveryManager;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.NodeDeliveryManager;

public class WorkingNode {
    String nodeId;
    String brokerName;

    NodeState state;
    NodeMessageDigester digester;
    MessageSender messageSender;
    DeliveryManager deliveryManager;


    public void consumerLoop() throws Exception {
        deliveryManager.initialize();
        // ask new messages
        try {
            while (true) {
                state.accept(digester, deliveryManager.getNextDelivery());
            }
        }
        finally {
            deliveryManager.close();
        }
    }

    public WorkingNode(String nodeId,
                       String brokerName,
                       MessageSender messageSender) {
        this.digester = new NodeMessageDigester(this);
        this.state = new NodeState(this);

        this.nodeId = nodeId;
        this.brokerName = brokerName;
        this.messageSender = messageSender;
        this.deliveryManager = new NodeDeliveryManager(state, nodeId);
    }

    public String getNodeId() {
        return nodeId;
    }

    static public boolean sameNode(String lhs, String rhs) {
        return lhs.equals(rhs);
    }
}
