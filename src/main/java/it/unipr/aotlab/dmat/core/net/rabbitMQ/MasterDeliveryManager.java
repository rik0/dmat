package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.messages.Messages;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class MasterDeliveryManager implements it.unipr.aotlab.dmat.core.net.MasterDeliveryManager {
    int serialNo;
    String masterId;
    QueueingConsumer queueingConsumer;
    Channel channel;
    ArrayList<EnvelopedMessageBody> answers = new ArrayList<EnvelopedMessageBody>();

    public MasterDeliveryManager(String masterId) {
        this.masterId = masterId;
    }

    @Override
    public Message getNextDelivery() throws Exception {
        EnvelopedMessageBody delivery = null;

        while (delivery == null) {
            if ((delivery = getDelivery()) == null) {
                EnvelopedMessageBody newDelivery = EnvelopedMessageBody
                    .parseFrom(queueingConsumer.nextDelivery().getBody());

                System.err.println("XXX delivery serial no: " + newDelivery
                        .getSerialNo());

                answers.add(newDelivery);
            }
        }

        return Messages.readMessage(delivery);
    }

    private EnvelopedMessageBody getDelivery() {
        Collections.sort(answers, new Message.EnvelopedSerialComparator());

        EnvelopedMessageBody delivery = null;
        Iterator<EnvelopedMessageBody> ianswer = answers.iterator();
        while (delivery == null && ianswer.hasNext()) {
            EnvelopedMessageBody message = ianswer.next();
            Assertion.isTrue(
                    message.getMessageKind() == Message.MessageKind.ANSWER.tag,
                    "Unexpected kind of message!");

            if (message.getSerialNo() == serialNo) {
                delivery = message;
                ianswer.remove();
            }
        }

        return delivery;
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
    public void setInterestedSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }
}
