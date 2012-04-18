package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
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
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.registers.rabbitMQ.NodeWorkGroup;

public class SumMatrices {
    public static void main(String[] argv) {
       try {
            MessageSender messageSender = new MessageSender(new Connector(
                    new Address("127.0.0.1")));
            NodeWorkGroup register = new NodeWorkGroup(messageSender);
            Nodes nodes = new Nodes(register);

            Matrix A = Matrices.newBuilder()
                    .setName("A")
                    .setNofRows(20)
                    .setNofColumns(20)
                    .splitHorizzontalyChuck(null, 10, "Atop", "Abottom")
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix B = Matrices.newBuilder()
                    .setName("B")
                    .setNofColumns(20)
                    .setNofRows(20)
                    .splitVerticallyChuck(null, 10, "Bleft", "Bright")
                    .setElementType(TypeWire.ElementType.INT32).build();

            Node testNode = nodes.setNodeName("testNode").build();
            Node testNode2 = nodes.setNodeName("testNode2").build();

            Chunk ATop = A.getChunk("Atop");
            Chunk ABottom = A.getChunk("Abottom");

            Chunk BLeft = B.getChunk("Bleft");
            Chunk BRight = B.getChunk("Bright");

            ATop.assignChunkToNode(testNode);
            BLeft.assignChunkToNode(testNode);

            ABottom.assignChunkToNode(testNode2);
            BRight.assignChunkToNode(testNode2);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/square");

            b.setMatrixId("A").setChunkId("Atop");
            testNode.sendMessage(new MessageSetMatrix(b));

            b.setMatrixId("A").setChunkId("Abottom");
            testNode2.sendMessage(new MessageSetMatrix(b));

            b.setMatrixId("B").setChunkId("Bleft");
            testNode.sendMessage(new MessageSetMatrix(b));

            b.setMatrixId("B").setChunkId("Bright");
            testNode2.sendMessage(new MessageSetMatrix(b));

            ATop.sendMessageExposeValues();
            ABottom.sendMessageExposeValues();

            BLeft.sendMessageExposeValues();
            BRight.sendMessageExposeValues();

            AdditionAssignment r = new AdditionAssignment();

            r.setComputingNodes(testNode);
            r.setOperands(A, B);
            r.exec();

            ATop.sendMessageExposeValues();
            ABottom.sendMessageExposeValues();

            MessageSender.closeConnection();
        } catch (Throwable e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
