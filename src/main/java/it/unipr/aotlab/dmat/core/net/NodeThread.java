package it.unipr.aotlab.dmat.core.net;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

public class NodeThread implements Node, Runnable {
    private final List<Message> messages = Collections
            .synchronizedList(new LinkedList<Message>());
    private Vector<Node> companions = null;
    
    NodeThread() {
    }

    @Override
    public void run() {
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

    
    @Override
    public Vector<Node> getCluster() {
        return companions;
    }

    @Override
    public void setCluster(Vector<Node> v) {
        this.companions = v;
    }
}
