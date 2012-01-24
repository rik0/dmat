package it.unipr.aotlab.dmat.core.net.rabbitMQ;

public class MessagesSetValueGeneric extends MessagesSetValue {
    static {
        Messages.messageFactories.put(
                MessageAssignChunkToNode.class.getSimpleName(),
                new MessagesSetValueGeneric());
    }

    private MessagesSetValueGeneric() {
    }
}
