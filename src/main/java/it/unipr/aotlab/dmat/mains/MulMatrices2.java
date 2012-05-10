package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.matrices.Multiplication;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.messages.MessageExposeValues;
import it.unipr.aotlab.dmat.core.net.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.net.messages.MessageShutdown;
import it.unipr.aotlab.dmat.core.net.zeroMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

public class MulMatrices2 {
    public static void main(String[] argv) {
        NodeWorkGroup register = new NodeWorkGroup("master", new IPAddress("192.168.0.2", 5672));

       try {
            Nodes nodes = new Nodes(register);

            Node testNode = nodes.setNodeName("testNode")
                    .setNodeAddress(new IPAddress("192.168.0.2", 6000)).build();
            Node testNode2 = nodes.setNodeName("testNode2")
                    .setNodeAddress(new IPAddress("192.168.0.2", 6002)).build();

            register.initialize();

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
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix C = Matrices.newBuilder()
                    .setName("C")
                    .setNofRows(15)
                    .setNofColumns(20)
                    .splitVerticallyChuck(null, 10, "Cleft", "Cright")
                    .setElementType(TypeWire.ElementType.INT32).build();

            A.getChunk(null).assignChunkToNode(testNode);

            B.getChunk("Bleft").assignChunkToNode(testNode2);
            B.getChunk("Bright").assignChunkToNode(testNode);

            C.getChunk("Cleft").assignChunkToNode(testNode);
            C.getChunk("Cright").assignChunkToNode(testNode2);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e2");

            b.setMatrixId(B.getMatrixId());
            testNode2.sendMessage(new MessageSetMatrix(b));
            testNode.sendMessage(new MessageSetMatrix(b));

            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e1");
            b.setMatrixId(C.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b));
            testNode2.sendMessage(new MessageSetMatrix(b));

            MatrixPieceOwnerBody.Builder mp = MatrixPieceOwnerBody.newBuilder();
            testNode.sendMessage(new MessageExposeValues(mp.setMatrixId("C").setChunkId("Cleft")));
            testNode2.sendMessage(new MessageExposeValues(mp.setMatrixId("C").setChunkId("Cright")));

            testNode2.sendMessage(new MessageExposeValues(mp.setMatrixId("B").setChunkId("Bleft")));
            testNode.sendMessage(new MessageExposeValues(mp.setMatrixId("B").setChunkId("Bright")));

            Multiplication r = new Multiplication();

            r.setOperands(A, B, C);
            r.exec();

            testNode.sendMessage(new MessageExposeValues(mp.setMatrixId("A").setChunkId("default")));

            testNode.sendMessage(new MessageShutdown());
            testNode2.sendMessage(new MessageShutdown());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            register.close();
        }
    }
}
