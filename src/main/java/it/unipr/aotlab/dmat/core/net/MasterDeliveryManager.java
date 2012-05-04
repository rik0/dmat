package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.messages.Messages;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public abstract class MasterDeliveryManager extends DeliveryManager {
    int serialNo;
    ArrayList<EnvelopedMessageBody> answers = new ArrayList<EnvelopedMessageBody>();

    @Override
    public Message getNextDelivery() throws Exception {
        EnvelopedMessageBody delivery = null;

        while ((delivery = getDelivery()) == null) {
            EnvelopedMessageBody newDelivery = awaitNextDelivery();

            answers.add(newDelivery);
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

            if (isCorrectMessage(message)) {
                delivery = message;
                ianswer.remove();
            }
        }

        return delivery;
    }

    public void setInterestedSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    protected boolean isCorrectMessage(EnvelopedMessageBody possibleDelivery) {
        return possibleDelivery.getSerialNo() == serialNo;
    }
}
