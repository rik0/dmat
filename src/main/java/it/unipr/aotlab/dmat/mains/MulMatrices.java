package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Compare;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.matrices.Multiplication;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.net.messages.MessageShutdown;
import it.unipr.aotlab.dmat.core.net.zeroMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

public class MulMatrices {
    public static void main(String[] argv) {
        NodeWorkGroup register = NodeWorkGroup.builder().
                masterId("master").
                masterAddress(new IPAddress("192.168.0.2", 5672)).build();

       try {
            Nodes nodes = new Nodes(register);

            Node testNode = nodes.setNodeName("testNode")
                    .setNodeAddress(new IPAddress("192.168.0.2", 6000)).build();

            Node testNode2 = nodes.setNodeName("testNode2")
                    .setNodeAddress(new IPAddress("192.168.0.2", 6002)).build();

            Node testNode3 = nodes.setNodeName("testNode3")
                    .setNodeAddress(new IPAddress("192.168.0.2", 6003)).build();

            register.initialize();


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

            A.getChunk(null).sendMessageExposeValues();

            Compare c = new Compare();
            c.setOperands(Expected, A);
            c.exec();

            System.err.println("Equals: " + c.answer());

            testNode.sendMessage(new MessageShutdown());
            testNode2.sendMessage(new MessageShutdown());
            testNode3.sendMessage(new MessageShutdown());

        } catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            register.close();
        }
    }
}
