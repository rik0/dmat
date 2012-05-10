package it.unipr.aotlab.dmat.core.net.zeroMQ;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.util.Utils;

import java.io.IOException;

import org.zeromq.ZMQ;

public class MasterDeliveryManager extends it.unipr.aotlab.dmat.core.net.MasterDeliveryManager {
    MessageReader messageReader;
    Thread messageReaderThread;

    public MasterDeliveryManager(ZMQ.Context context,
                                 String port) {
        this.messageReader = new MessageReader(context, port);
    }

    @Override
    public void close() throws IOException {
        messageReader.stop();
        Utils.awaitThreadDeath(messageReaderThread);
    }

    @Override
    public void initialize() throws IOException {
        messageReader.initialize();
        this.messageReaderThread = new Thread(messageReader);
        this.messageReaderThread.start();
    }

    @Override
    protected EnvelopedMessageBody awaitNextDelivery() throws Exception {
        return messageReader.awaitNextDelivery();
    }
}
