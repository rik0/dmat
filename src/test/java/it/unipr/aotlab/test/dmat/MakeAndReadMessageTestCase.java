package it.unipr.aotlab.test.dmat;

import static junit.framework.Assert.assertEquals;
import it.unipr.aotlab.dmat.core.generated.MessageTestBody;
import it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody;
import it.unipr.aotlab.dmat.core.net.Message;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageTest;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.Messages;

import java.io.IOException;

import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;

public class MakeAndReadMessageTestCase {

    @Test
    public void makeAndReadMessage() throws InvalidProtocolBufferException {
        String messageOnTheWire = "Test message";

        MessageTestBody.Body.Builder builder = MessageTestBody.Body.newBuilder()
                .setDestination(NodeListBody.getDefaultInstance())
                .setMessage(messageOnTheWire);
        MessageTest messageTestSent = new MessageTest(builder);

        Message message = Messages.readMessage(
                messageTestSent.contentType(), messageTestSent.message());

        try {
            message.accept(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageTest messageTestReceived = (MessageTest) message;
        assertEquals(messageTestReceived.body().getMessage(), messageOnTheWire);
    }
}
