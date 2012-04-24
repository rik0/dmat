package it.unipr.aotlab.dmat.mains;

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

public class MulMatrices3 {
    public static void main(String[] argv) {
       NodeWorkGroup register = null;
       try {
            register = new NodeWorkGroup(new Address(), "master");
            Nodes nodes = new Nodes(register);
            Node testNode = nodes.setNodeName("testNode").build();

            Matrix Expected = Matrices.newBuilder()
                    .setName("Expected")
                    .setNofRows(20)
                    .setNofColumns(20)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix A = Matrices.newBuilder()
                    .setName("A")
                    .setNofRows(20)
                    .setNofColumns(20)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix B = Matrices.newBuilder()
                    .setName("B")
                    .setNofRows(20)
                    .setNofColumns(20)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix C = Matrices.newBuilder()
                    .setName("C")
                    .setNofRows(20)
                    .setNofColumns(20)
                    .setElementType(TypeWire.ElementType.INT32).build();


            Expected.getChunk(null).assignChunkToNode(testNode);
            A.getChunk(null).assignChunkToNode(testNode);
            B.getChunk(null).assignChunkToNode(testNode);
            C.getChunk(null).assignChunkToNode(testNode);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir")
                               + "/example_matrices/square");

            b.setMatrixId(B.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b));

            b.setMatrixId(C.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b));

            b.setURI("file://" + System.getProperty("user.dir")
                               + "/example_matrices/square_squared");
            b.setMatrixId(Expected.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b));

            Multiplication r = new Multiplication();
            r.setOperands(A, B, C);
            r.exec();

            Compare c = new Compare();
            c.setOperands(A, Expected);
            c.exec();

            System.err.println("Equals? " + c.answer());

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            register.close();
        }
    }
}
