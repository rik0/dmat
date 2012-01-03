package it.unipr.aotlab.dmat.core.net.rabbitMQ;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.Message;

public class Node implements it.unipr.aotlab.dmat.core.net.Node {
    String nodeId;
    private Map<String, Chunk> chunks;
    Connector connector;

    Node() {
    }

    Map<String, Chunk> Chunks() {
        if (chunks == null)
            chunks = new HashMap<String, Chunk>();

        return chunks;
    }

    @Override
    public Chunk getChunck(String id) throws ChunkNotFound {
        Chunk chunk = chunks.get(id);
        if (chunk == null) {
            throw new ChunkNotFound();
        }

        return chunk;
    }

    @Override
    public void sendMessage(Message m) throws IOException {
        //it might be better using the same channel multiple times?
        Channel channel = connector.connection().createChannel();
        try {
            channel.queueDeclare(nodeId, false, true, true, null);
            Builder propBuilder = new AMQP.BasicProperties.Builder();
            propBuilder.contentType(m.contentType());
            channel.basicPublish("amq.direct", nodeId, propBuilder.build(),
                    m.message());
        } finally {
            channel.close();
        }
    }
}
