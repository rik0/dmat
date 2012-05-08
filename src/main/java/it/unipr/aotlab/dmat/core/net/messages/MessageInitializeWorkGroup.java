package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.NodeWorkGroupWire.NodeWorkGroupBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.MessageOrder;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.util.Collection;

import com.google.protobuf.ByteString;

public class MessageInitializeWorkGroup extends MessageOrder {
    NodeWorkGroupBody.Builder builder = null;
    NodeWorkGroupBody realBody = null;

    public NodeWorkGroupBody body() {
        if (realBody == null) {
            realBody = builder.build();
            builder = null;
        }

        return realBody;
    }

    MessageInitializeWorkGroup(NodeWorkGroupBody body) {
        realBody = body;
    }

    public MessageInitializeWorkGroup(NodeWorkGroupBody.Builder builder) {
        this.builder = builder;
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
    public Collection<String> recipients() {
        return body().getDestination().getNodeIdList();
    }

    @Override
    public Message recipients(Collection<String> recipients) {
        builder.setDestination(list2Protobuf(recipients));
        return this;
    }
}
