package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import com.google.protobuf.ByteString;

public abstract class Message {
    public static class EnvelopedSerialComparator implements Comparator<EnvelopedMessageBody> {
        @Override
        public int compare(EnvelopedMessageBody lhs, EnvelopedMessageBody rhs) {
            return lhs.getSerialNo() - rhs.getSerialNo();
        }
    }

    int serialNo = -1;

    public enum MessageKind {
        IMMEDIATE(0x0),
        SUPPORT(0x1),
        ORDER(0x2),
        ANSWER(0x4);

        public int tag;
        private MessageKind(int tag) {
            this.tag = tag;
        }
    }

    public String contentType() {
        return this.getClass().getSimpleName();
    }

    public abstract MessageKind messageKind();

    public abstract ByteString message();

    public abstract Collection<String> recipients();

    public Message recipients(String... recipients) {
        return recipients(Arrays.asList(recipients));
    }

    public abstract Message recipients(Collection<String> recipients);

    public static NodeListBody list2Protobuf(Collection<String> recipients) {
        return NodeListBody.newBuilder().addAllNodeId(recipients).build();
    }

    public Message serialNo(int serialNo) {
        this.serialNo = serialNo;
        return this;
    }

    public int serialNo() {
        if (serialNo == -1)
            throw new DMatInternalError("serialNo still unset!");

        return serialNo;
    }

    public abstract void accept(NodeMessageDigester digester)
            throws IOException;
}
