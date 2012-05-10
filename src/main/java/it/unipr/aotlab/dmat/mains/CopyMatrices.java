package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Compare;
import it.unipr.aotlab.dmat.core.matrices.CopyMatrix;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.net.messages.MessageShutdown;
import it.unipr.aotlab.dmat.core.net.zeroMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

public class CopyMatrices {
    public static void main(String[] argv) {
       NodeWorkGroup register = new NodeWorkGroup("master", new IPAddress("192.168.0.2", 5672));

       try {
            Nodes nodes = new Nodes(register);

            Node testNode = nodes
                    .setNodeName("testNode")
                    .setNodeAddress(new IPAddress("192.168.0.2", 6000)).build();

            Node testNode2 = nodes
                    .setNodeName("testNode2")
                    .setNodeAddress(new IPAddress("192.168.0.2", 6002)).build();

            register.initialize();

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

            Chunk ATop = A.getChunk("Atop");
            Chunk ABottom = A.getChunk("Abottom");

            Chunk BLeft = B.getChunk("Bleft");
            Chunk BRight = B.getChunk("Bright");

            ATop.assignChunkToNode(testNode);
            BLeft.assignChunkToNode(testNode);

            ABottom.assignChunkToNode(testNode2);
            BRight.assignChunkToNode(testNode2);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/square2");

            b.setMatrixId("A");
            testNode.sendMessage(new MessageSetMatrix(b));
            testNode2.sendMessage(new MessageSetMatrix(b));

            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/square");
            b.setMatrixId("B");
            testNode.sendMessage(new MessageSetMatrix(b));
            testNode2.sendMessage(new MessageSetMatrix(b));

            ATop.sendMessageExposeValues();
            ABottom.sendMessageExposeValues();

            BLeft.sendMessageExposeValues();
            BRight.sendMessageExposeValues();

            Compare c = new Compare();
            c.setOperands(A, B);
            c.exec();
            System.err.println("A and B are equals? " + c.answer());

            CopyMatrix r = new CopyMatrix();
            r.setOperands(A, B);
            r.exec();

            Compare c2 = new Compare();
            c2.setOperands(A, B);
            c2.exec();

            System.err.println("A and B are equals? " + c.answer());

            ATop.sendMessageExposeValues();
            ABottom.sendMessageExposeValues();

            testNode.sendMessage(new MessageShutdown());
            testNode2.sendMessage(new MessageShutdown());
        } catch (Throwable e) {
            System.err.println(e);
            e.printStackTrace();
        }
       finally {
            register.close();
       }
    }
}
