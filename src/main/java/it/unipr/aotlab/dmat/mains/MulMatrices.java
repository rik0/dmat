package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.matrices.Multiplication;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.registers.NodeRegister;

public class MulMatrices {
    public static void main(String[] argv) {
       try {
            MessageSender messageSender = new MessageSender(new Connector(
                    new Address("127.0.0.1")));
            NodeRegister register = new NodeRegister(messageSender);
            Nodes nodes = new Nodes(register);

            Node testNode = nodes.setNodeName("testNode").build();
            Node testNode2 = nodes.setNodeName("testNode2").build();
            Node testNode3 = nodes.setNodeName("testNode3").build();

            Matrix A = Matrices.newBuilder()
                    .setName("A")
                    .setNofRows(10)
                    .setNofColumns(20)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix B = Matrices.newBuilder()
                    .setName("B")
                    .setNofRows(10)
                    .setNofColumns(15)
                    .splitVerticallyChuck(null, 10, "Bleft", "Bright")
                    .splitHorizzontalyChuck("Bright", 4, "Bright-top", "Bright-bottom")
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix C = Matrices.newBuilder()
                    .setName("C")
                    .setNofRows(15)
                    .setNofColumns(20)
                    .splitVerticallyChuck(null, 10, "Cleft", "Cright")
                    .splitHorizzontalyChuck("Cleft", 6, "Cleft-top", "Cleft-bottom")
                    .splitVerticallyChuck("Cleft-bottom", 5, "Cleft-bottom-left", "Cleft-bottom-right")
                    .splitVerticallyChuck("Cright", 15, "Cright-left", "Cright-right")
                    .splitHorizzontalyChuck("Cright-right", 4, "Cright-right-top", "Cright-right-bottom")
                    .setElementType(TypeWire.ElementType.INT32).build();

            A.getChunk(null).assignChunkToNode(testNode3);

            B.getChunk("Bleft").assignChunkToNode(testNode2);
            B.getChunk("Bright-top").assignChunkToNode(testNode);
            B.getChunk("Bright-bottom").assignChunkToNode(testNode3);

            C.getChunk("Cleft-top").assignChunkToNode(testNode);
            C.getChunk("Cleft-bottom-left").assignChunkToNode(testNode2);
            C.getChunk("Cleft-bottom-right").assignChunkToNode(testNode);
            C.getChunk("Cright-left").assignChunkToNode(testNode2);
            C.getChunk("Cright-right-top").assignChunkToNode(testNode);
            C.getChunk("Cright-right-bottom").assignChunkToNode(testNode2);

            Thread.sleep(2000);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e2");
            b.setMatrixId(B.getMatrixId());
            b.setChunkId("Bleft");
            testNode2.sendMessage(new MessageSetMatrix(b.build()));

            b.setChunkId("Bright-top");
            testNode.sendMessage(new MessageSetMatrix(b.build()));

            b.setChunkId("Bright-bottom");
            testNode3.sendMessage(new MessageSetMatrix(b.build()));

            b.setMatrixId(C.getMatrixId());
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e1");
            b.setChunkId("Cleft-top");
            testNode.sendMessage(new MessageSetMatrix(b.build()));

            b.setChunkId("Cleft-bottom-left");
            testNode2.sendMessage(new MessageSetMatrix(b.build()));

            b.setChunkId("Cleft-bottom-right");
            testNode.sendMessage(new MessageSetMatrix(b.build()));

            b.setChunkId("Cright-left");
            testNode2.sendMessage(new MessageSetMatrix(b.build()));

            b.setChunkId("Cright-right-top");
            testNode.sendMessage(new MessageSetMatrix(b.build()));

            b.setChunkId("Cright-right-bottom");
            testNode2.sendMessage(new MessageSetMatrix(b.build()));

            Thread.sleep(2000);

            Multiplication r = new Multiplication();

            r.setOperands(A, B, C);
            r.exec();

            Thread.sleep(2000);

            A.getChunk(null).sendMessageExposeValues();

            MessageSender.closeConnection();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
