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
import it.unipr.aotlab.dmat.core.registers.NodeRegister;

import java.io.IOException;

public class MulMatrices2 {
    public static void main(String[] argv) {
       try {
           MatrixPieceOwnerBody.Builder mp = MatrixPieceOwnerBody.newBuilder();
            MessageSender messageSender = new MessageSender(new Connector(
                    new Address("127.0.0.1")));
            NodeRegister register = new NodeRegister(messageSender);
            Nodes nodes = new Nodes(register);

            Node testNode = nodes.setNodeName("testNode").build();
            Node testNode2 = nodes.setNodeName("testNode2").build();

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
            b.setChunkId("Bleft");
            testNode2.sendMessage(new MessageSetMatrix(b.build()));

            b.setChunkId("Bright");
            testNode.sendMessage(new MessageSetMatrix(b.build()));

            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/e1");
            b.setMatrixId(C.getMatrixId());
            b.setChunkId("Cleft");
            testNode.sendMessage(new MessageSetMatrix(b.build()));

            b.setChunkId("Cright");
            testNode2.sendMessage(new MessageSetMatrix(b.build()));

            Thread.sleep(2000);
            testNode.sendMessage(new MessageExposeValues(mp.setMatrixId("C").setChunkId("Cleft").build()));
            testNode2.sendMessage(new MessageExposeValues(mp.setMatrixId("C").setChunkId("Cright").build()));

            testNode2.sendMessage(new MessageExposeValues(mp.setMatrixId("B").setChunkId("Bleft").build()));
            testNode.sendMessage(new MessageExposeValues(mp.setMatrixId("B").setChunkId("Bright").build()));

            Thread.sleep(2000);
            Multiplication r = new Multiplication();

            r.setOperands(A, B, C);
            r.exec();

            Thread.sleep(2000);

            testNode.sendMessage(new MessageExposeValues(mp.setMatrixId("A").setChunkId("default").build()));

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
