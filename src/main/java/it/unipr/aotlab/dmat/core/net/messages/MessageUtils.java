package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Message;

public class MessageUtils {
    public static EnvelopedMessageBody putInEnvelope(Message message) {
        return EnvelopedMessageBody
                .newBuilder()
                .setSerialNo(message.serialNo())
                .setContentType(message.contentType())
                .setMessageKind(message.messageKind().tag)
                .setMessage(message.message())
                .build();
    }
}