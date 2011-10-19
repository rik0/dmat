package it.unipr.aotlab.dmat.core.net;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class NodeThread implements Node, Runnable {
    private final List<Message> messages = Collections
            .synchronizedList(new LinkedList<Message>());

    NodeThread() {
    }

    @Override
    public void run() {
        popMessage();
    }

    @Override
    public synchronized void sendMessage(final Message m) {
        messages.add(m);
        notify();
    }
    
    public synchronized Message popMessage() {
        while (messages.isEmpty()) {
            try {
                wait();
            } catch (final InterruptedException e) {
            }
        }
        return messages.remove(0);
    }
}
