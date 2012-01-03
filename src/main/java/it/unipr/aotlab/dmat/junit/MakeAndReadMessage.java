package it.unipr.aotlab.dmat.junit;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageTest;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageTestBody;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Messages;

import org.junit.*;
import static junit.framework.Assert.*;

import com.google.protobuf.InvalidProtocolBufferException;

public class MakeAndReadMessage {
    Messages factory = new Messages();

    @Test
    public void makeAndReadMessage() throws InvalidProtocolBufferException {
        String messageOnTheWire = "Test message";

        MessageTestBody.Body body = MessageTestBody.Body.newBuilder()
                .setMessage(messageOnTheWire).build();
        MessageTest messageTestSent = new MessageTest(body);

        Message message = Messages.readMessage(
                messageTestSent.contentType(), messageTestSent.message());

        message.exec();

        MessageTest messageTestReceived = (MessageTest) message;
        assertEquals(messageTestReceived.body.getMessage(), messageOnTheWire);
    }
}
