package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.AdditionAssignment;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.NodeRegister;
import java.io.IOException;

public class BuildAMatrix {
    public static void main(String[] argv) {
       try {
            MessageSender messageSender = new MessageSender(new Connector(
                    new Address("127.0.0.1")));
            NodeRegister register = new NodeRegister(messageSender);
            Nodes nodes = new Nodes(register);

            Matrix matrix = Matrices.newBuilder().setName("A")
                    .setNofColumns(10).setNofRows(10)
                    .setElementType(TypeWire.ElementType.INT32).build();
            
            Matrix matrix2 = Matrices.newBuilder().setName("B")
                    .setNofColumns(10).setNofRows(10)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Node node = nodes.setNodeName("testNode").build();

            Chunk chunk = matrix.getChunk(null);
            Chunk chunk2 = matrix2.getChunk(null);
            
            chunk.assignChunkToNode(node);
            chunk2.assignChunkToNode(node);
            
            AdditionAssignment r = new AdditionAssignment();
            r.setOperands(matrix, matrix2);
            r.exec();

            MessageSender.closeConnection();
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
