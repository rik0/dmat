package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;

import java.io.IOException;

import org.zeromq.ZMQ;

public class MasterDeliveryManager extends it.unipr.aotlab.dmat.core.net.MasterDeliveryManager {
    ZMQ.Context context;
    String port;
    ZMQ.Socket messageGetter;

    public MasterDeliveryManager(ZMQ.Context context, String port) {
        this.context = context;
        this.port = port;
    }

    @Override
    public void close() throws IOException {
        messageGetter.close();
    }

    @Override
    public void initialize() throws IOException {
        this.messageGetter = context.socket(ZMQ.REQ);
        this.messageGetter.bind("tcp://*:" + port);
    }

    @Override
    protected EnvelopedMessageBody awaitNextDelivery() throws Exception {
        return EnvelopedMessageBody
                .parseFrom(messageGetter.recv(0));
    }
}
