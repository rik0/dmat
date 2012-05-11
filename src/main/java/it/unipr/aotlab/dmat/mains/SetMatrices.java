package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.messages.MessageSetMatrix;
import it.unipr.aotlab.dmat.core.net.zeroMQ.Nodes;
import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;

public class SetMatrices {
    public static void main(String[] argv) {
        NodeWorkGroup register = NodeWorkGroup.builder().
                masterId("master").
                masterAddress(new IPAddress("192.168.0.2", 5672)).build();
       try {
            Nodes nodes = new Nodes(register);

            Matrix matrix = Matrices.newBuilder()
                    .setName("A")
                    .setNofRows(20)
                    .setNofColumns(20)
                    .setElementType(TypeWire.ElementType.INT32).build();

            Node testNode = nodes.setNodeName("testNode")
                                 .setNodeAddress(new IPAddress("192.168.0.2", 6000))
                                 .build();

            register.initialize();

            matrix.getChunk(null).assignChunkToNode(testNode);

            OrderSetMatrixBody.Builder b = OrderSetMatrixBody.newBuilder();
            b.setMatrixId(matrix.getMatrixId());
            b.setURI("file://" + System.getProperty("user.dir") + "/example_matrices/square");

            testNode.sendMessage(new MessageSetMatrix(b));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            register.close();
        }
    }
}
