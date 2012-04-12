package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.matrices.Multiplication;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageExposeValues;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.registers.rabbitMQ.NodeWorkGroup;

import java.io.IOException;

public class MulMatrices3 {
    public static void main(String[] argv) {
       try {
            MessageSender messageSender = new MessageSender(new Connector(
                    new Address("127.0.0.1")));
            NodeWorkGroup register = new NodeWorkGroup(messageSender);
            Nodes nodes = new Nodes(register);

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

            Node testNode = nodes.setNodeName("testNode").build();
            A.getChunk(null).assignChunkToNode(testNode);
            B.getChunk(null).assignChunkToNode(testNode);
            C.getChunk(null).assignChunkToNode(testNode);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/square");

            b.setMatrixId(B.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b));
            b.setMatrixId(C.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b));

            Thread.sleep(2000);

            Multiplication r = new Multiplication();
            r.setOperands(A, B, C);
            r.exec();

            Thread.sleep(5000);

            MatrixPieceOwnerBody.Builder mp = MatrixPieceOwnerBody.newBuilder();
            testNode.sendMessage(new MessageExposeValues(mp.setMatrixId("A")));

            MessageSender.closeConnection();
        } catch (ChunkNotFound e) {
            e.printStackTrace();
        } catch (IdNotUnique e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DMatError e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
