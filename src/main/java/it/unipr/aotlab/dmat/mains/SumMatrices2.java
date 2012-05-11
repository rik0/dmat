package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.AdditionAssignment;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.messages.MessageExposeValues;
import it.unipr.aotlab.dmat.core.net.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.net.zeroMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

public class SumMatrices2 {
    public static void main(String[] argv) {
        NodeWorkGroup register = NodeWorkGroup.builder().
                masterId("master").
                masterAddress(new IPAddress("192.168.0.2", 5672)).build();

       try {
            Nodes nodes = new Nodes(register);
            Node testNode = nodes.setNodeName("testNode")
                    .setNodeAddress(new IPAddress("192.168.0.2", 6000))
                    .build();

            register.initialize();

            Matrix A = Matrices.newBuilder()
                    .setName("A")
                    .setNofRows(20)
                    .setNofColumns(20)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Matrix B = Matrices.newBuilder()
                    .setName("B")
                    .setNofColumns(20)
                    .setNofRows(20)
                    .setElementType(TypeWire.ElementType.INT32).build();

            A.getChunk(null).assignChunkToNode(testNode);
            B.getChunk(null).assignChunkToNode(testNode);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir")
                               + "/example_matrices/square");

            b.setMatrixId(A.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b));
            b.setMatrixId(B.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b));

            AdditionAssignment r = new AdditionAssignment();
            r.setOperands(A, B);
            r.exec();

            MatrixPieceOwnerBody.Builder mp = MatrixPieceOwnerBody.newBuilder();
            testNode.sendMessage(new MessageExposeValues(mp.setMatrixId("A")));

        } catch (Throwable e) {
            e.printStackTrace();
        }
        finally{
            register.close();
        }
    }
}
