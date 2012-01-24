package it.unipr.aotlab.dmat.core.net.rabbitMQ;

public class MessagesSetValueInt32 extends MessagesSetValue {
    static {
        Messages.messageFactories.put(
                MessageSetValueInt32.class.getSimpleName(),
                new MessagesSetValueInt32());
    }
}
