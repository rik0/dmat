package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageAssignChunkToNode;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.NodeRegister;

import java.io.IOException;

public class BuildAMatrix {
    public static void main(String[] argv) {
        NodeRegister register = new NodeRegister();
        Nodes nodes = new Nodes(register);
        Connector connector = new Connector(new Address("127.0.0.1"));

        Node node;
        try {

            Matrix matrix = Matrices.newBuilder()
                    .setName("A")
                    .setNofColumns(21)
                    .setNofRows(20)
                    .setElementType(ChunkDescription.ElementType.BOOL)
                    .build();

            node = nodes.setNodeName("testNode").setConnector(connector)
                    .build();

            Chunk chunk = matrix.getChunk(null);
            chunk.assignChunkToNode(node);

            connector.connection().close();
        } catch (ChunkNotFound e) {
            e.printStackTrace();
        } catch (IdNotUnique e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DMatError e) {
            e.printStackTrace();
        }
    }
}
