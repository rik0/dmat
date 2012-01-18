package it.unipr.aotlab.dmat.core;

import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageAssignChunkToNode;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageShutdown;

public class NodeMessageDigester {
    WorkingNode hostWorkingNode;

    NodeMessageDigester(WorkingNode hostWorkingNode) {
        this.hostWorkingNode = hostWorkingNode;
    }

    private void debugMessage(Message message) {
        System.err.print("Received " + message.getClass().getCanonicalName()
                + ", ");
    }

    public void accept(MessageAssignChunkToNode message) {
        debugMessage(message);
        System.err.println("adding chunk to list of managed.");

        InNodeChunk<?> newChunk = InNodeChunks.build(new Chunk(message.body));
        hostWorkingNode.state.managedChunks.add(newChunk);
    }

    public void accept(MessageShutdown message) {
        debugMessage(message);
        System.err.println("terminating.");

        throw new MainNode.Quit();
    }

    public void accept(Message message) {
        debugMessage(message);
        System.err.println("ignoring.");
    }
}
