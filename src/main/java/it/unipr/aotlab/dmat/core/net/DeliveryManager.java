package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.messages.Messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public abstract class DeliveryManager {
    public abstract void close() throws IOException;

    public abstract void initialize() throws IOException;

    protected abstract EnvelopedMessageBody awaitNextDelivery() throws Exception;

    //a multiset might be better!
    ArrayList<EnvelopedMessageBody> sortingBuffer= new ArrayList<EnvelopedMessageBody>();

    public Message getNextDelivery() throws Exception {
        EnvelopedMessageBody delivery = null;
        do {
            if ((delivery = findNextProcessableMessage()) == null) {
                EnvelopedMessageBody newDelivery = awaitNextDelivery();
                System.err.println("XXX Serial no " + newDelivery.getSerialNo());
                sortingBuffer.add(newDelivery);
            }

        } while (delivery == null);

        return Messages.readMessage(delivery);
    }

    private EnvelopedMessageBody findNextProcessableMessage() {
        Collections.sort(sortingBuffer, new Message.EnvelopedSerialComparator());

        Iterator<EnvelopedMessageBody> i = sortingBuffer.iterator();
        EnvelopedMessageBody delivery = null;

        while (delivery == null && i.hasNext()) {
            EnvelopedMessageBody possibleDelivery = i.next();
            if (isCorrectMessage(possibleDelivery)) {
                delivery = possibleDelivery;
                i.remove();
            }
        }

        return delivery;
    }

    protected abstract boolean isCorrectMessage(EnvelopedMessageBody possibleDelivery);
}
