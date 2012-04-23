package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Compare;
import it.unipr.aotlab.dmat.core.matrices.CopyMatrix;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.registers.rabbitMQ.NodeWorkGroup;

public class CopyMatrices {
    public static void main(String[] argv) {
       NodeWorkGroup register = null;
       try {
            register = new NodeWorkGroup(new Address(), "master");
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

            CopyMatrix r = new CopyMatrix();

            r.setOperands(A, B);
            r.exec();

            Compare c = new Compare();
            c.setOperands(A, B);
            c.exec();

            System.err.println("A and B are equals? " + c.answer());

            ATop.sendMessageExposeValues();
            ABottom.sendMessageExposeValues();


        } catch (Throwable e) {
            System.err.println(e);
            e.printStackTrace();
        }
       finally {
            register.close();
       }
    }
}
