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
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.rabbitMQ.NodeWorkGroup;

public class SumMatrices2 {
    public static void main(String[] argv) {
       try {
            NodeWorkGroup register = new NodeWorkGroup(new IPAddress(), "master");
            Nodes nodes = new Nodes(register);

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

            Node testNode = nodes.setNodeName("testNode").build();

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

            MessageSender.closeConnection();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
