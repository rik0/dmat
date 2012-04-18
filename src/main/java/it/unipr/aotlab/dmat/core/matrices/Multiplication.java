package it.unipr.aotlab.dmat.core.matrices;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPiece;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPieceListBody;
import it.unipr.aotlab.dmat.core.generated.OrderMultiplyWire.OrderMultiply;
import it.unipr.aotlab.dmat.core.generated.OrderMultiplyWire.OrderMultiplyBody;
import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceListWire.SendMatrixPieceListBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire.TypeBody;
import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.messages.MessageMultiply;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class Multiplication extends Operation {
    @Override
    public int arity() {
        return 3;
    }

    @Override
    protected void otherPreconditions() throws DMatError {
        Matrix outputMatrix = operands.get(0);

        Matrix firstOperand = operands.get(1);
        Matrix secondOperand = operands.get(2);

        if ( ! (firstOperand.getNofCols() == secondOperand.getNofRows()
                && outputMatrix.getNofRows() == firstOperand.getNofRows()
                && outputMatrix.getNofCols() == secondOperand.getNofCols())) {
            throw new DMatError("Invalid matrix sizes for multiplication.");
        }
    }

    @Override
    protected ArrayList<WorkZone> neededChunksToUpdateThisChunk(Chunk outputMatrixChunk) {
        Matrix firstOperand = operands.get(1);
        Matrix secondOperand = operands.get(2);

        ArrayList<NeededPieceOfChunk> firstOpChunks
            = firstOperand.involvedChunksAllCols(outputMatrixChunk.getStartRow(),
                                                 outputMatrixChunk.getEndRow());

        Collections.sort(firstOpChunks, new Comparator<NeededPieceOfChunk>() {
            @Override
            public int compare(NeededPieceOfChunk lhs, NeededPieceOfChunk rhs) {
                int rv = lhs.chunk.getStartRow() - rhs.chunk.getStartRow();
                if (rv == 0) rv = lhs.chunk.getEndRow() - rhs.chunk.getEndRow();
                return rv;
            }});

        ArrayList<NeededPieceOfChunk> secondOpChunks
            = secondOperand.involvedChunksAllRows(outputMatrixChunk.getStartCol(),
                                                  outputMatrixChunk.getEndCol());

        Collections.sort(secondOpChunks, new Comparator<NeededPieceOfChunk>() {
            @Override
            public int compare(NeededPieceOfChunk lhs, NeededPieceOfChunk rhs) {
                int rv = lhs.chunk.getStartCol() - rhs.chunk.getStartCol();
                if (rv == 0) rv = lhs.chunk.getEndCol() - rhs.chunk.getEndCol();
                return rv;
            }
        });

        return makeWorkzones(outputMatrixChunk, firstOpChunks, secondOpChunks);
    }

    private ArrayList<WorkZone> makeWorkzones(Chunk outputMatrixChunk,
                                              ArrayList<NeededPieceOfChunk> firstOpChunks,
                                              ArrayList<NeededPieceOfChunk> secondOpChunks) {
        /* Creates workzones using first operand row division and
         * second operand columns division. */
        Matrix firstOperand = operands.get(1);
        Matrix secondOperand = operands.get(2);

        ArrayList<WorkZone> workZones = new ArrayList<WorkZone>();

        int rowGroupIndex = 0;
        int colGroupIndex = 0;
        Rectangle outputArea = new Rectangle();

        do {
            // loop over columns
            rowGroupIndex = 0;
            NeededPieceOfChunk secondOpChunk = secondOpChunks.get(colGroupIndex);
            fillInOutputCols(outputArea, outputMatrixChunk, secondOpChunk.chunk);

            ArrayList<NeededPieceOfChunk> secondOpNeededChunks
                = secondOperand.involvedChunksAllRows(outputArea.startCol,
                                                      outputArea.endCol);
            do {
                // loop over rows
                NeededPieceOfChunk firstOpChunk = firstOpChunks.get(rowGroupIndex);
                fillInOutputRows(outputArea, outputMatrixChunk, firstOpChunk.chunk);

                ArrayList<NeededPieceOfChunk> neededChunks = new ArrayList<NeededPieceOfChunk>();

                neededChunks.addAll(firstOperand
                        .involvedChunksAllCols(outputArea.startRow,
                                               outputArea.endRow));
                neededChunks.addAll(secondOpNeededChunks);

                workZones.add(new WorkZone(outputMatrixChunk, new Rectangle(outputArea), neededChunks));
            } while (-1 != (rowGroupIndex = getNextRowGroup(rowGroupIndex, firstOpChunks)));
        } while (-1 != (colGroupIndex = getNextColGroup(colGroupIndex, secondOpChunks)));

        return workZones;
    }

    private static void fillInOutputRows(Rectangle output, Chunk outputChunk, Chunk firstOp) {
        output.startRow = Math.max(outputChunk.getStartRow(), firstOp.getStartRow());
        output.endRow   = Math.min(outputChunk.getEndRow(), firstOp.getEndRow());
    }

    private static void fillInOutputCols(Rectangle output, Chunk outputChunk, Chunk secondOp) {
        output.startCol = Math.max(outputChunk.getStartCol(), secondOp.getStartCol());
        output.endCol   = Math.min(outputChunk.getEndCol(), secondOp.getEndCol());
    }

    private static int getNextRowGroup(int startFrom, ArrayList<NeededPieceOfChunk> firstOpChunks) {
        int end = firstOpChunks.size();
        int currentEndRow = firstOpChunks.get(startFrom).chunk.getEndRow();
        int currentStartRow;

        int index = startFrom + 1;

        while (index < end) {
            currentStartRow = firstOpChunks.get(index).chunk.getStartRow();
            if (currentEndRow <= currentStartRow) {
                break;
            }
            ++index;
        } //else
        if (index >= end)
            return -1;

        return index;
    }

    private static int getNextColGroup(int startFrom, ArrayList<NeededPieceOfChunk> secondOpChunks) {
        int end = secondOpChunks.size();
        int currentEndCol = secondOpChunks.get(startFrom).chunk.getEndCol();
        int currentStartCol;

        int index = startFrom + 1;

        while (index < end) {
            currentStartCol = secondOpChunks.get(index).chunk.getStartCol();
            if (currentEndCol <= currentStartCol) {
                break;
            }
            ++index;
        } //else
        if (index >= end)
            return -1;

        return index;
    }


    @Override
    protected void sendOperationsOrders() throws IOException {
        TreeSet<String> unusedNodes = new TreeSet<String>();
        unusedNodes.addAll(getNodeWorkGroup().nodesId());

        sendMessagesToComputingNodes(unusedNodes);
        sendMessagesToStorageNodes(unusedNodes);
        sendMessagesToUnusedNodes(unusedNodes);
    }

    private void sendMessagesToComputingNodes(TreeSet<String> unusedNodes)
            throws IOException {
        OrderMultiply.Builder operation = OrderMultiply.newBuilder();

        Matrix output   = operands.get(0);
        Matrix firstOp  = operands.get(1);
        Matrix secondOp = operands.get(2);

        TypeBody type = TypeBody.newBuilder()
                .setElementType(firstOp.getElementType())
                .setSemiRing(firstOp.getSemiRing()).build();

        operation.setOutputMatrixId(output.getMatrixId());
        operation.setFirstFactorMatrixId(firstOp.getMatrixId());
        operation.setSecondFactorMatrixId(secondOp.getMatrixId());
        operation.setType(type);

        for (NodeWorkZonePair nwzp : tasks) {
            Node computingNode = nwzp.computingNode;
            boolean tr = unusedNodes.remove(computingNode.getNodeId());
            Assertion.isTrue(tr, "");

            OrderMultiplyBody.Builder order = OrderMultiplyBody.newBuilder();
            MatrixPieceListBody.Builder
                missingPieces = MatrixPieceListBody.newBuilder();

            for (WorkZone wz : nwzp.workZones) {
                fillInOperation(order, wz, operation);
                updateMissingPieces(missingPieces, wz, computingNode);
            }
            order.setMissingPieces(missingPieces.build());
            fillInPieces2BeSent(order, computingNode.getNodeId());
            fillInPiecesAwaitingUpdate(order, computingNode.getNodeId());


            getNodeWorkGroup().sendOrderRaw(
                    (new MessageMultiply(order)).serialNo(serialNo),
                    computingNode.getNodeId());
        }
    }

    private void sendMessagesToStorageNodes(TreeSet<String> unusedNodes)
            throws IOException {
        for (Matrix matrix : this.operands) {
            for (Chunk chunk : matrix.getChunks()) {
                String nodeId = chunk.getAssignedNode().getNodeId();
                if (unusedNodes.remove(nodeId)) {
                    System.err.println("XXX storage node: " + nodeId);
                    OrderMultiplyBody.Builder order
                        = OrderMultiplyBody.newBuilder();

                    fillInPieces2BeSent(order, nodeId);
                    fillInPiecesAwaitingUpdate(order, nodeId);
                    order.setMissingPieces(MatrixPieceListBody
                            .getDefaultInstance());

                    getNodeWorkGroup()
                        .sendOrderRaw((new MessageMultiply(order))
                        .serialNo(serialNo), nodeId);
                }
            }
        }
    }

    private void fillInPiecesAwaitingUpdate(OrderMultiplyBody.Builder order,
                                            String nodeId) {
        MatrixPieceListBody.Builder list = this.pieces2await.get(nodeId);
        if (list == null) {
            list = MatrixPieceListBody.newBuilder();
        }

        order.setAwaitingUpdates(list.build());
    }

    private void fillInPieces2BeSent(OrderMultiplyBody.Builder order,
                                     String nodeId) {
        SendMatrixPieceListBody.Builder list = this.pieces2beSent.get(nodeId);
        if (list == null) {
            list = SendMatrixPieceListBody.newBuilder();
        }

        order.setPiecesToSend(list.build());
    }

    private void updateMissingPieces(MatrixPieceListBody.Builder missingPieces,
                                     WorkZone wz,
                                     Node computingNode) {
        MatrixPiece.Builder missingPiece = MatrixPiece.newBuilder();
        for (NeededPieceOfChunk c : wz.involvedChunks) {
            if ( ! computingNode.doesManage(c.chunk.chunkId)) {
                missingPiece.setMatrixId(c.chunk.getMatrixId())
                            .setPosition(c.piece.convertToProto());
                missingPieces.addMatrixPiece(missingPiece.build());
            }
        }
    }

    private static void fillInOperation(OrderMultiplyBody.Builder order,
                                        WorkZone wz,
                                        OrderMultiply.Builder operation) {
        operation.setOutputNodeId(wz.outputChunk.getAssignedNodeId());
        operation.setOutputPosition(wz.outputArea.convertToProto());

        order.addOperation(operation.build());
    }
}

