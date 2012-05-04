package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class NodeDeliveryManager extends it.unipr.aotlab.dmat.core.net.NodeDeliveryManager {
    String nodeId;
    QueueingConsumer queueingConsumer;
    Channel channel;

    public NodeDeliveryManager(NodeState currentState, String nodeId) {
        super(currentState);
        this.nodeId = nodeId;
    }

    @Override
    public void initialize() throws IOException {
        channel = MessageSender.getConnection().createChannel();

        channel.queueDeclare(nodeId, false, false, false, null);
        queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(nodeId, true, queueingConsumer);
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    protected EnvelopedMessageBody awaitNextDelivery() throws Exception {
        return EnvelopedMessageBody.parseFrom(queueingConsumer.nextDelivery().getBody());
    }
}
