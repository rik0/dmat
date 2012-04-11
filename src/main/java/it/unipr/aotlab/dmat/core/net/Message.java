package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody;
import it.unipr.aotlab.dmat.core.workingnode.NodeMessageDigester;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public abstract class Message {
    int serialNo = -1;

    public enum MessageKind {
        IMMEDIATE(0x0),
        SUPPORT(0x1),
        ORDER(0x2);

        public int tag;
        private MessageKind(int tag) {
            this.tag = tag;
        }
    }

    public String contentType() {
        return this.getClass().getSimpleName();
    }

    public abstract MessageKind messageType();

    public abstract byte[] message();

    public abstract Collection<String> recipients();

    public void recipients(String... recipients) {
        recipients(Arrays.asList(recipients));
    }

    public abstract void recipients(Collection<String> recipients);

    public static NodeListBody list2Protobuf(Collection<String> recipients) {
        return NodeListBody.newBuilder().addAllNodeId(recipients).build();
    }

    public void serialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public int serialNo() {
        if (serialNo == -1)
            throw new DMatInternalError("serialNo still unset!");

        return serialNo;
    }

    public abstract void accept(NodeMessageDigester digester)
            throws IOException;
}
