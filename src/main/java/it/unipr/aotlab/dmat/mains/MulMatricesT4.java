package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.matrices.Multiplication;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.net.zeroMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

public class MulMatricesT4 {
    public static void main(String[] argv) {
        NodeWorkGroup register = NodeWorkGroup.builder().
                masterId("master").
                masterAddress(new IPAddress("42.191.37.74", 5672)).build();

       try {
            Nodes nodes = new Nodes(register);

            Node testNode0 = nodes.setNodeName("testnode0")
                    .setNodeAddress(new IPAddress("42.191.37.74", 6000)).build();
            
            Node testNode1 = nodes.setNodeName("testnode1")
                    .setNodeAddress(new IPAddress("42.191.37.74", 6001)).build();
            
            Node testNode2 = nodes.setNodeName("testnode2")
                    .setNodeAddress(new IPAddress("42.191.37.74", 6002)).build();
            
            Node testNode3 = nodes.setNodeName("testnode3")
                    .setNodeAddress(new IPAddress("42.191.37.74", 6003)).build();

            register.initialize();

            Matrix A = Matrices.newBuilder()
                    .setName("A")
                    .setNofRows(1000)
                    .setNofColumns(1000)
                    .splitVerticallyChuck(null, 500, "Aleft", "Aright")
                    .splitHorizzontalyChuck("Aleft", 500, "ATopLeft", "ATopRight")
                    .splitHorizzontalyChuck("Aright", 500, "ABottomLeft", "ABottomRight")
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix B = Matrices.newBuilder()
                    .setName("B")
                    .setNofRows(1000)
                    .setNofColumns(1000)
                    .splitVerticallyChuck(null, 500, "Bleft", "Bright")
                    .splitHorizzontalyChuck("Bleft", 500, "BTopLeft", "BTopRight")
                    .splitHorizzontalyChuck("Bright", 500, "BBottomLeft", "BBottomRight")
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix C = Matrices.newBuilder()
                    .setName("C")
                    .setNofRows(1000)
                    .setNofColumns(1000)
                    .splitVerticallyChuck(null, 500, "Cleft", "Cright")
                    .splitHorizzontalyChuck("Cleft", 500, "CTopLeft", "CTopRight")
                    .splitHorizzontalyChuck("Cright", 500, "CBottomLeft", "CBottomRight")
                    .setElementType(TypeWire.ElementType.INT32).build();

            A.getChunk("ATopLeft").assignChunkToNode(testNode0);
            A.getChunk("ATopRight").assignChunkToNode(testNode1);
            A.getChunk("ABottomLeft").assignChunkToNode(testNode2);
            A.getChunk("ABottomRight").assignChunkToNode(testNode3);

            B.getChunk("BTopLeft").assignChunkToNode(testNode0);
            B.getChunk("BTopRight").assignChunkToNode(testNode1);
            B.getChunk("BBottomLeft").assignChunkToNode(testNode2);
            B.getChunk("BBottomRight").assignChunkToNode(testNode3);
            
            C.getChunk("CTopLeft").assignChunkToNode(testNode0);
            C.getChunk("CTopRight").assignChunkToNode(testNode1);
            C.getChunk("CBottomLeft").assignChunkToNode(testNode2);
            C.getChunk("CBottomRight").assignChunkToNode(testNode3);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/large");

            b.setMatrixId(B.getMatrixId());
            testNode0.sendMessage(new MessageSetMatrix(b));
            testNode1.sendMessage(new MessageSetMatrix(b));
            testNode2.sendMessage(new MessageSetMatrix(b));
            testNode3.sendMessage(new MessageSetMatrix(b));

            b.setMatrixId(C.getMatrixId());
            testNode0.sendMessage(new MessageSetMatrix(b));
            testNode1.sendMessage(new MessageSetMatrix(b));
            testNode2.sendMessage(new MessageSetMatrix(b));
            testNode3.sendMessage(new MessageSetMatrix(b));

            Multiplication r = new Multiplication();

            r.setOperands(A, B, C);
            r.exec();

            register.shutDown();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            register.close();
        }
    }
}