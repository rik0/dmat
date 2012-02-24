package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceOwnerWire.MatrixPieceOwnerBody;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssign;
import it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageAddAssign;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

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
        Matrix secondOperand = operands.get(1);

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
        OrderAddAssign.Builder subOrder = OrderAddAssign.newBuilder();

        subOrder.setFirstAddendumMatrixId(operands.get(0).getMatrixId());
        subOrder.setSecondAddendumMatrixId(operands.get(1).getMatrixId());

        for (NodeWorkZonePair nodeAndworkZone : workers) {
            Node computingNode = nodeAndworkZone.computingNode;

            MatrixPieceOwnerBody.Builder missingMatrices = MatrixPieceOwnerBody.newBuilder();
            OrderAddAssignBody.Builder order = OrderAddAssignBody.newBuilder();

            TreeSet<String> missingChunks = new TreeSet<String>();

            for (WorkZone wz : nodeAndworkZone.workZone) {
                subOrder.setOutputPiece(wz.outputArea.convertToProto());
                order.addOperation(subOrder.build());

                for (Chunk c : wz.involvedChunks) {
                    if ( ! computingNode.doesManage(c.getChunkId())
                            && missingChunks.add(c.matrixId + "." + c.chunkId)) {
                        missingMatrices.setChunkId(c.getChunkId());
                        missingMatrices.setMatrixId(c.getMatrixId());
                    }
                }
                order.addMissingPieces(missingMatrices.build());
            }

            // XXX we need to inform the node of the kind of
            // semiring.
            computingNode.sendMessage(new MessageAddAssign(order.build()));
        }
    }
}
