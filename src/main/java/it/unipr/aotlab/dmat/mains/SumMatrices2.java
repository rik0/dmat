package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.AdditionAssignment;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageExposeValues;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.registers.NodeRegister;

import java.io.IOException;

public class SumMatrices2 {
    public static void main(String[] argv) {
       try {
            MessageSender messageSender = new MessageSender(new Connector(
                    new Address("127.0.0.1")));
            NodeRegister register = new NodeRegister(messageSender);
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
            b.setURI("file:///home/paolo/uni/dissertation/example_matrix");
            
            b.setMatrixId(A.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b.build()));
            b.setMatrixId(B.getMatrixId());
            testNode.sendMessage(new MessageSetMatrix(b.build()));

            Thread.sleep(2000);

            AdditionAssignment r = new AdditionAssignment();
            r.setOperands(A, B);
            r.exec();

            Thread.sleep(2000);

            MatrixPieceOwnerBody.Builder mp = MatrixPieceOwnerBody.newBuilder();
            testNode.sendMessage(new MessageExposeValues(mp.setMatrixId("A").build()));

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
