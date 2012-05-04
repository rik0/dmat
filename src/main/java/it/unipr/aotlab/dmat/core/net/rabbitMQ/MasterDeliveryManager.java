package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class MasterDeliveryManager extends it.unipr.aotlab.dmat.core.net.MasterDeliveryManager {
    String masterId;
    QueueingConsumer queueingConsumer;
    Channel channel;

    public MasterDeliveryManager(String masterId) {
        this.masterId = masterId;
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public void initialize() throws IOException {
        channel = MessageSender.getConnection().createChannel();

        channel.queueDeclare(masterId, false, false, false, null);
        queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(masterId, true, queueingConsumer);
    }

    @Override
    protected EnvelopedMessageBody awaitNextDelivery() throws Exception {
        return EnvelopedMessageBody
                .parseFrom(queueingConsumer.nextDelivery().getBody());
    }
}
