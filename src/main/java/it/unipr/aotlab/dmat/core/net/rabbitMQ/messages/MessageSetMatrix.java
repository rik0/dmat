package it.unipr.aotlab.dmat.core.net.rabbitMQ.messages;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageOrder;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.util.Collection;

import com.google.protobuf.ByteString;

public class MessageSetMatrix extends MessageOrder {
    private OrderSetMatrixBody realBody;
    public OrderSetMatrixBody.Builder builder;

    MessageSetMatrix(OrderSetMatrixBody body) {
        this.realBody = body;
    }

    public MessageSetMatrix(OrderSetMatrixBody.Builder builder) {
        this.builder = builder;
    }

    public OrderSetMatrixBody body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }
        return realBody;
    }

    @Override
    public ByteString message() {
        return body().toByteString();
    }

    @Override
    public void accept(NodeMessageDigester digester) {
        digester.accept(this);
    }

    @Override
    public Message recipients(Collection<String> recipients) {
        System.err.println("XXX " +recipients + " - " + builder);
        builder.setDestination(list2Protobuf(recipients));
        return this;
    }

    @Override
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }
}
