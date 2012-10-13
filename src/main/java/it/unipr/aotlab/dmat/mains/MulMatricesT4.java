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
    private static final String IP = "192.168.1.160";

    private static final String B_TOP = "BTop";
    private static final String B_BOTTOM = "BRight";

    private static final String B_TOP_LEFT = "BTopLeft";
    private static final String B_TOP_RIGHT = "BTopRight";
    private static final String B_BOTTOM_RIGHT = "BBottomRight";
    private static final String B_BOTTOM_LEFT = "BBottomLeft";

    private static final String C_TOP = "CTop";
    private static final String C_BOTTOM = "CBottom";
    private static final String C_TOP_LEFT = "CTopLeft";
    private static final String C_BOTTOM_LEFT = "CBottomLeft";
    private static final String C_TOP_RIGHT = "CTopRight";
    private static final String C_BOTTOM_RIGHT = "CBottomRight";

    public static void main(String[] argv) {
        NodeWorkGroup register = NodeWorkGroup.builder().
                masterId("master").
                masterAddress(new IPAddress(IP, 5672)).build();

       try {
            Nodes nodes = new Nodes(register);

            Node testNode0 = nodes.setNodeName("testnode0")
                    .setNodeAddress(new IPAddress(IP, 6000)).build();

            Node testNode1 = nodes.setNodeName("testnode1")
                    .setNodeAddress(new IPAddress(IP, 6001)).build();

            Node testNode2 = nodes.setNodeName("testnode2")
                    .setNodeAddress(new IPAddress(IP, 6002)).build();

            Node testNode3 = nodes.setNodeName("testnode3")
                    .setNodeAddress(new IPAddress(IP, 6003)).build();

            register.initialize();

            Matrix A = Matrices.newBuilder()
                    .setName("A")
                    .setNofRows(400)
                    .setNofColumns(400)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix B = Matrices.newBuilder()
                    .setName("B")
                    .setNofRows(400)
                    .setNofColumns(400)
                    .splitHorizzontalyChuck(null, 200, B_TOP, B_BOTTOM)
                    .splitVerticallyChuck(B_BOTTOM, 200, B_BOTTOM_LEFT, B_BOTTOM_RIGHT)
                    .splitVerticallyChuck(B_TOP, 200, B_TOP_LEFT, B_TOP_RIGHT)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix C = Matrices.newBuilder()
                    .setName("C")
                    .setNofRows(400)
                    .setNofColumns(400)
                    .splitHorizzontalyChuck(null, 200, C_TOP, C_BOTTOM)
                    .splitVerticallyChuck(C_BOTTOM, 200, C_BOTTOM_LEFT, C_BOTTOM_RIGHT)
                    .splitVerticallyChuck(C_TOP, 200, C_TOP_LEFT, C_TOP_RIGHT)
                    .setElementType(TypeWire.ElementType.INT32).build();

            A.getChunk(null).assignChunkToNode(testNode0);

            B.getChunk(B_TOP_LEFT).assignChunkToNode(testNode0);
            B.getChunk(B_TOP_RIGHT).assignChunkToNode(testNode1);
            B.getChunk(B_BOTTOM_LEFT).assignChunkToNode(testNode2);
            B.getChunk(B_BOTTOM_RIGHT).assignChunkToNode(testNode3);

            C.getChunk(C_TOP_LEFT).assignChunkToNode(testNode1);
            C.getChunk(C_TOP_RIGHT).assignChunkToNode(testNode2);
            C.getChunk(C_BOTTOM_LEFT).assignChunkToNode(testNode3);
            C.getChunk(C_BOTTOM_RIGHT).assignChunkToNode(testNode0);

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
