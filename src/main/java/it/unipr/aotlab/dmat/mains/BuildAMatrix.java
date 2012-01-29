package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.errors.IdNotUnique;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceTripletsInt32;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Nodes;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageSetValueInt32;
import it.unipr.aotlab.dmat.core.registers.NodeRegister;

import java.io.IOException;
import java.util.Iterator;

public class BuildAMatrix {
    public static void main(String[] argv) {
        try {
            MessageSender messageSender = new MessageSender(new Connector(new Address("127.0.0.1")));
            NodeRegister register = new NodeRegister(messageSender);
            Nodes nodes = new Nodes(register);

            Matrix matrix = Matrices.newBuilder().setName("A")
                    .setNofColumns(10).setNofRows(10)
                    .setElementType(ChunkDescription.ElementType.INT32).build();

            Node node = nodes.setNodeName("testNode").build();

            Chunk chunk = matrix.getChunk(null);
            chunk.assignChunkToNode(node);
            
            MatrixPieceTripletsInt32Wire.Body.Builder b = MatrixPieceTripletsInt32Wire.Body
                    .newBuilder();
            b.setMatrixName("A");
            for (int i = 0; i < 10; ++i) {
                b.addValues(MatrixPieceTripletsInt32Wire.Triplet.newBuilder()
                        .setCol(i).setRow(i).setValue(i).build());
            }
            
            MatrixPieceTripletsInt32Wire.Body messsageBody = b.build();
            node.sendMessage(new MessageSetValueInt32(messsageBody));

            MessageSender.closeConnection();
        } catch (ChunkNotFound e) {
            e.printStackTrace();
        } catch (IdNotUnique e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DMatError e) {
            e.printStackTrace();
        }
    }
}
