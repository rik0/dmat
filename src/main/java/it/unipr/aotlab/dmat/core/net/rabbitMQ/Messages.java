package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.QueueingConsumer;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.net.Message;

public class Messages {
    static Map<String, Messages> messageFactories = new HashMap<String, Messages>();

    public Message parseMessage(byte[] rawMessage) {
        throw new DMatInternalError(
                "Invalid call to parseMessage! Unknown message type?");
    }

    public static Messages getFactory(String contentType) {
        Messages m = messageFactories.get(contentType);
        if (m == null)
            throw new DMatInternalError("Unknown content type:" + contentType);
        
        return m;
    }
    
    public static Message readMessage(QueueingConsumer.Delivery envelopedMessage) {
        return readMessage(envelopedMessage.getProperties().getContentType(), envelopedMessage.getBody());
    }
    
    static Message readMessage(String contentType, byte[] rawMessage) {
        return getFactory(contentType).parseMessage(rawMessage);
    }
}
