package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AdditionAssignment extends Operation {
    @Override
    public int arity() {
        return 2;
    }

    @Override
    protected void otherPreconditions() throws DMatError {
        Matrix firstOperand = operands.get(0);
        Matrix secondOperand = operands.get(1);

        if ( ! (firstOperand.getNofRows() == secondOperand.getNofRows()
                && secondOperand.getNofCols() == secondOperand.getNofCols()))
            throw new DMatError("The two AdditionAssignment operators must"
                                + " have the same size!");
    }

    @Override
    protected List<WorkZone> neededChunksToUpdateThisChunk(Chunk outputMatrixChunk) {
        LinkedList<WorkZone> workZones = new LinkedList<WorkZone>();
        Matrix secondOperand = operands.get(2);

        for (Chunk chunk : secondOperand.involvedChunks(outputMatrixChunk.getArea())) {
            updateWorkZones(workZones, outputMatrixChunk, chunk);
        }

        return workZones;
    }

    private void updateWorkZones(LinkedList<WorkZone> workZones, Chunk firstOpChunk, Chunk secondOpChunk) {
        Rectangle intersection;

        if (null != (intersection = firstOpChunk.intersection(secondOpChunk))) {
            List<Chunk> involvedChunks = new LinkedList<Chunk>();
            involvedChunks.add(firstOpChunk);
            involvedChunks.add(secondOpChunk);
            WorkZone workzone = new WorkZone(intersection, involvedChunks);

            workZones.add(workzone);
        }
    }

    @Override
    protected void sendOrdersToWorkers() throws IOException {
        OrderAddAssignBody.Builder orderBuilder = OrderAddAssignBody.newBuilder()
            .setFirstAddendumMatrixId(operands.get(0).getId())
            .setSecondAddendumMatrixId(operands.get(1).getId());

        for (NodeWorkZonePair nodeAndworkZone : workers) {
            Node node = nodeAndworkZone.node;

            for (WorkZone wz : nodeAndworkZone.workZone) {
                orderBuilder.setFirstAddendumNodeId(wz.involvedChunks.get(0).chunkId)
                    .setSecondAddendumNodeId(wz.involvedChunks.get(1).chunkId)
                    .setOutputPiece(wz.outputArea.convertToProto());
                node.sendMessage(new MessageAddAssign(orderBuilder.build()));
            }
        }
    }
}
