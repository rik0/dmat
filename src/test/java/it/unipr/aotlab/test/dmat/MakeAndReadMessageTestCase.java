package it.unipr.aotlab.test.dmat;

import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageTest;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.AGPBCMessageTestBody;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Messages;

import org.junit.*;
import static junit.framework.Assert.*;

import com.google.protobuf.InvalidProtocolBufferException;

public class MakeAndReadMessageTestCase {
    Messages factory = new Messages();

    @Test
    public void makeAndReadMessage() throws InvalidProtocolBufferException {
        String messageOnTheWire = "Test message";

        AGPBCMessageTestBody.Body body = AGPBCMessageTestBody.Body.newBuilder()
                .setMessage(messageOnTheWire).build();
        MessageTest messageTestSent = new MessageTest(body);

        Message message = Messages.readMessage(
                messageTestSent.contentType(), messageTestSent.message());

        message.exec();

        MessageTest messageTestReceived = (MessageTest) message;
        assertEquals(messageTestReceived.body.getMessage(), messageOnTheWire);
    }
}
