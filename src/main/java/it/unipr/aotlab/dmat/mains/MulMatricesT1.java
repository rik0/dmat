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

public class MulMatricesT1 {
    public static void main(String[] argv) {
        NodeWorkGroup register = NodeWorkGroup.builder().
                masterId("master").
                masterAddress(new IPAddress("42.191.37.74", 5672)).build();

       try {
            Nodes nodes = new Nodes(register);

            Node testNode0 = nodes.setNodeName("testnode0")
                    .setNodeAddress(new IPAddress("42.191.37.74", 6000)).build();

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
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix C = Matrices.newBuilder()
                    .setName("C")
                    .setNofRows(400)
                    .setNofColumns(400)
                    .setElementType(TypeWire.ElementType.INT32).build();

            A.getChunk(null).assignChunkToNode(testNode0);
            B.getChunk(null).assignChunkToNode(testNode0);
            C.getChunk(null).assignChunkToNode(testNode0);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/large");

            b.setMatrixId(B.getMatrixId());
            testNode0.sendMessage(new MessageSetMatrix(b));

            b.setMatrixId(C.getMatrixId());
            testNode0.sendMessage(new MessageSetMatrix(b));

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