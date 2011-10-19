package it.unipr.aotlab.dmat.core.net;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class NodeThread implements Node, Runnable {
    @SuppressWarnings("unchecked")
    private final Queue<Message> messages = ((Queue<Message>) Collections
            .synchronizedList(new LinkedList<Message>()));

    NodeThread() {
    }

    @Override
    public void run() {
        /*
         * Here we will active the message loop
         */
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
            } catch (InterruptedException e) {
            }
        }
        return messages.poll();
    }
}
