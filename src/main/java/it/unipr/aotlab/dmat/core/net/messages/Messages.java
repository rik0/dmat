package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.util.ForceLoad;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public abstract class Messages {
    static Map<String, Messages> messageFactories = new HashMap<String, Messages>();

    static {
        ForceLoad.listFromFile(Messages.class, "KindOfMessages");
    }

    public abstract Message parseMessage(ByteString rawMessage)
            throws InvalidProtocolBufferException;

    public static Messages getFactory(String contentType) {
        Messages m = messageFactories.get(contentType);
        if (m == null)
            throw new DMatInternalError("Unknown content type: " + contentType);

        return m;
    }

    public static Message readMessage(String contentType, ByteString rawMessage)
            throws InvalidProtocolBufferException {
        System.err.println("XXX contentType " + contentType);
        return getFactory(contentType).parseMessage(rawMessage);
    }

    public static Message readMessage(EnvelopedMessageBody delivery)
        throws InvalidProtocolBufferException {
        Message m = readMessage(delivery.getContentType(), delivery.getMessage());
        m.serialNo(delivery.getSerialNo());

        return m;
    }
}
