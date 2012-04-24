package it.unipr.aotlab.test.dmat;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Compare;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.matrices.Multiplication;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.registers.rabbitMQ.NodeWorkGroup;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;


public class ProductTestCase {
    static String rabbitMQAddress = "localhost";
    static ArrayList<Process> nodes = new ArrayList<Process>();

    public static void startNode(String name) throws IOException {
        String[] execi = { "/usr/bin/sakura", "-h", "-e", "./bin/worknode.sh", name, "master", rabbitMQAddress };
        nodes.add(Runtime.getRuntime().exec(execi));
    }

    public static void stopAllNodes() throws InterruptedException, IOException {
        String[] execi = { "/usr/bin/sakura", "-h", "-e", "./bin/resetrabbitmq.sh" };
        int rv = Runtime.getRuntime().exec(execi).waitFor();
        Assert.assertEquals(0, rv);

        for (Process n : nodes) {
            n.waitFor();
        }
        nodes.clear();
    }

    @Test
    public void complexMul() {
        try {
            startNode("testNode");
            startNode("testNode2");
            startNode("testNode3");
            Thread.sleep(2000);

            NodeWorkGroup register = new NodeWorkGroup(new Address(), "master");
            Nodes nodes = new Nodes(register);

            Node testNode = nodes.setNodeName("testNode").build();
            Node testNode2 = nodes.setNodeName("testNode2").build();
            Node testNode3 = nodes.setNodeName("testNode3").build();

            Matrix Expected = Matrices.newBuilder()
                    .setName("A")
                    .setNofRows(10)
                    .setNofColumns(20)
                    .setElementType(TypeWire.ElementType.INT32).build();

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

            Expected.getChunk(null).assignChunkToNode(testNode3);

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

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e2");
            b.setMatrixId(B.getMatrixId());
            testNode2.sendMessage(new MessageSetMatrix(b));
            testNode.sendMessage(new MessageSetMatrix(b));
            testNode3.sendMessage(new MessageSetMatrix(b));

            b.setMatrixId(C.getMatrixId());
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e1");
            testNode.sendMessage(new MessageSetMatrix(b));
            testNode2.sendMessage(new MessageSetMatrix(b));
            testNode.sendMessage(new MessageSetMatrix(b));
            testNode2.sendMessage(new MessageSetMatrix(b));
            testNode.sendMessage(new MessageSetMatrix(b));
            testNode2.sendMessage(new MessageSetMatrix(b));

            b.setMatrixId(Expected.getMatrixId());
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e2xe1");
            testNode3.sendMessage(new MessageSetMatrix(b));

            Multiplication r = new Multiplication();
            r.setOperands(A, B, C);
            r.exec();

            Compare c = new Compare();
            c.setOperands(Expected, A);
            c.exec();

            Assert.assertTrue(c.answer());

            register.close();

        } catch (Throwable e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
        finally {
            try {
                stopAllNodes();
            } catch (Throwable e) {
                e.printStackTrace();
                Assert.assertTrue(false);
            }
        }
    }
}
