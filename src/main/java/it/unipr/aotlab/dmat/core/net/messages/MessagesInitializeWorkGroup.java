package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.OrderDummyWire.OrderDummyBody;
import it.unipr.aotlab.dmat.core.net.Message;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessagesInitializeWorkGroup extends Messages {
    static {
        Messages.messageFactories.put(
                MessageInitializeWorkGroup.class.getSimpleName(),
                new MessagesInitializeWorkGroup());
    }

    private MessagesInitializeWorkGroup() {
    }

    @Override
    public Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException {
        return new MessageShutdown(OrderDummyBody.parseFrom(rawMessage));
    }
}
