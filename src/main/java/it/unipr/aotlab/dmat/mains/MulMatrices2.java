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
import it.unipr.aotlab.dmat.core.net.zeroMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

public class MulMatrices2 {
    public static void main(String[] argv) {
        NodeWorkGroup register = NodeWorkGroup.builder().
                masterId("master").
                masterAddress(new IPAddress("192.168.1.160", 5672)).build();

       try {
            Nodes nodes = new Nodes(register);

            Node testNode = nodes.setNodeName("testNode")
                    .setNodeAddress(new IPAddress("192.168.1.160", 6000)).build();
            Node testNode1 = nodes.setNodeName("testNode1")
                    .setNodeAddress(new IPAddress("192.168.1.160", 6001)).build();
            Node testNode2 = nodes.setNodeName("testNode2")
                    .setNodeAddress(new IPAddress("192.168.1.160", 6002)).build();
            Node testNode3 = nodes.setNodeName("testNode3")
                    .setNodeAddress(new IPAddress("192.168.1.160", 6003)).build();

            register.initialize();

            Matrix Expected = Matrices.newBuilder()
                    .setName("Expected")
                    .setNofRows(10)
                    .setNofColumns(20)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix A = Matrices.newBuilder()
                    .setName("A")
                    .setNofRows(10)
                    .setNofColumns(20)
                    .splitHorizzontalyChuck(null, 7, "Atop", "Abottom")
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix B = Matrices.newBuilder()
                    .setName("B")
                    .setNofRows(10)
                    .setNofColumns(15)
                    .splitHorizzontalyChuck(null, 7, "Btop", "Bbottom")
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix C = Matrices.newBuilder()
                    .setName("C")
                    .setNofRows(15)
                    .setNofColumns(20)
                    .splitHorizzontalyChuck(null, 7, "Ctop", "Cbottom")
                    .setElementType(TypeWire.ElementType.INT32).build();

            Expected.getChunk(null).assignChunkToNode(testNode);

            A.getChunk("Atop").assignChunkToNode(testNode);
            A.getChunk("Abottom").assignChunkToNode(testNode3);

            B.getChunk("Btop").assignChunkToNode(testNode1);
            B.getChunk("Bbottom").assignChunkToNode(testNode2);

            C.getChunk("Ctop").assignChunkToNode(testNode1);
            C.getChunk("Cbottom").assignChunkToNode(testNode2);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e2xe1");
            b.setMatrixId(Expected.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b));

            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e2");
            b.setMatrixId(B.getMatrixId());
            testNode2.sendMessage(new MessageSetMatrix(b));
            testNode1.sendMessage(new MessageSetMatrix(b));

            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e1");
            b.setMatrixId(C.getMatrixId());
            testNode1.sendMessage(new MessageSetMatrix(b));
            testNode2.sendMessage(new MessageSetMatrix(b));

            Multiplication r = new Multiplication();
            r.setOperands(A, B, C);
            r.setComputingNodes(testNode, testNode3);
            r.exec();

            Compare c = new Compare();
            c.setOperands(A, Expected);
            c.exec();

            System.err.println("Equals: " + c.answer() + " (expected: true)");
            register.shutDown();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            register.close();
        }
    }
}
