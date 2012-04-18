package it.unipr.aotlab.test.dmat;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.InvalidCoord;
import it.unipr.aotlab.dmat.core.generated.ChunkDescriptionWire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire;
import it.unipr.aotlab.dmat.core.generated.MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body;
import it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import it.unipr.aotlab.dmat.core.matrices.Rectangle;
import it.unipr.aotlab.dmat.core.matrixPiece.Int32Triplet;
import it.unipr.aotlab.dmat.core.matrixPiece.MatrixPieceTripletsInt32;

import java.util.Iterator;

import junitx.util.PrivateAccessor;

import org.junit.Test;

public class MatricesTestCase {

    @Test
    public void buildMatrix() throws ChunkNotFound, SecurityException,
            NoSuchFieldException {
        Matrix matrix;

        matrix = Matrices
                .newBuilder()
                .setName("A")
                .setNofColumns(20)
                .setNofRows(30)
                .splitHorizzontalyChuck(null, 15, "top", "bottom")
                .splitVerticallyChuck("top", 10, "topleft", "topright")
                .setChunkFormat("topleft",
                        ChunkDescriptionWire.Format.COMPRESSEDCOLUMNS)
                .setElementType(TypeWire.ElementType.BOOL)
                .build();

        Chunk topleft = matrix.getChunk("topleft");
        Chunk topright = matrix.getChunk("topright");
        Chunk bottom = matrix.getChunk("bottom");

        assertEquals(topleft, matrix.getChunk(0, 0));
        assertEquals(topright, matrix.getChunk(0, 11));
        assertEquals(bottom, matrix.getChunk(16, 0));

        try {
            matrix.getChunk(30, 20);
            assertTrue(false);
        } catch (InvalidCoord e) {
        }
        try {
            matrix.getChunk(30, 0);
            assertTrue(false);
        } catch (InvalidCoord e) {
        }
        try {
            matrix.getChunk(0, 20);
            assertTrue(false);
        } catch (InvalidCoord e) {
        }
        try {
            matrix.getChunk(-1, 0);
            assertTrue(false);
        } catch (InvalidCoord e) {
        }
        try {
            matrix.getChunk(0, -1);
            assertTrue(false);
        } catch (InvalidCoord e) {
        }

        assertEquals("topleft", topleft.getChunkId());
        assertEquals(ChunkDescriptionWire.Format.COMPRESSEDCOLUMNS,
                PrivateAccessor.getField(topleft, "format"));

        assertEquals(0, topleft.getStartRow());
        assertEquals(0, topleft.getStartCol());

        assertEquals(15, topleft.getEndRow());
        assertEquals(10, topleft.getEndCol());

        assertEquals("bottom", bottom.getChunkId());
        assertEquals(ChunkDescriptionWire.Format.DENSE,
                PrivateAccessor.getField(bottom, "format"));

        assertEquals(0, topright.getStartRow());
        assertEquals(10, topright.getStartCol());

        assertEquals(15, topright.getEndRow());
        assertEquals(20, topright.getEndCol());

        assertEquals(ChunkDescriptionWire.Format.DENSE,
                PrivateAccessor.getField(topright, "format"));
        assertEquals("topright", topright.getChunkId());

        assertEquals(15, bottom.getStartRow());
        assertEquals(0, bottom.getStartCol());

        assertEquals(30, bottom.getEndRow());
        assertEquals(20, bottom.getEndCol());
    }

    @Test
    public void makeAPieceSendIterate() {
        MatrixPieceTripletsInt32Body.Builder b
                = MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body
                .newBuilder();

        b.setMatrixId("A");
        b.setUpdate(true);
        b.setNodeId("unused");
        b.setPosition(Rectangle.build(0, 0, 10, 10).convertToProto());
        b.setDestination(NodeListBody.getDefaultInstance());

        for (int i = 0; i < 10; ++i) {
            b.addValues(MatrixPieceTripletsInt32Wire.Triplet.newBuilder()
                    .setCol(i * 2).setRow(i * 3).setValue(i * 4).build());
        }
        MatrixPieceTripletsInt32Wire.MatrixPieceTripletsInt32Body messsageBody = b.build();

        MatrixPieceTripletsInt32.Builder receiverBuilder = new MatrixPieceTripletsInt32.Builder();

        @SuppressWarnings("unchecked")
        Iterator<Int32Triplet> resultsIterator = (Iterator<Int32Triplet>) receiverBuilder
                .buildFromMessageBody(messsageBody).matrixPieceIterator();

        int i = 0;
        while (resultsIterator.hasNext()) {
            Int32Triplet r = resultsIterator.next();
            assertEquals(r.col, i * 2);
            assertEquals(r.row, i * 3);
            assertEquals(r.value, i * 4);
            ++i;
        }
    }
}
