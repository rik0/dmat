package it.unipr.aotlab.test.dmat;

import it.unipr.aotlab.dmat.core.generated.MessageTestBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageTest;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Messages;

import org.junit.*;
import static junit.framework.Assert.*;

import com.google.protobuf.InvalidProtocolBufferException;

public class MakeAndReadMessageTestCase {

    @Test
    public void makeAndReadMessage() throws InvalidProtocolBufferException {
        String messageOnTheWire = "Test message";

        MessageTestBody.Body body = MessageTestBody.Body.newBuilder()
                .setMessage(messageOnTheWire).build();
        MessageTest messageTestSent = new MessageTest(body);

        Message message = Messages.readMessage(
                messageTestSent.contentType(), messageTestSent.message());

        message.exec(null);

        MessageTest messageTestReceived = (MessageTest) message;
        assertEquals(messageTestReceived.body.getMessage(), messageOnTheWire);
    }
}
