package it.unipr.aotlab.test.dmat;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.errors.InvalidCoord;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;
import junitx.util.PrivateAccessor;

import org.junit.Test;

public class MatricesTestCase {

    @Test
    public void buildMatrix() throws ChunkNotFound, SecurityException,
            NoSuchFieldException {
        Matrices matrixFactory = new Matrices();
        Matrix matrix;

        matrix = matrixFactory
                .setNofColumns(20)
                .setNofRows(30)
                .splitHorizzontalyChuck(null, 15, "top", "bottom")
                .splitVerticallyChuck("top", 10, "topleft", "topright")
                .setChunkFormat("topleft",
                        ChunkDescription.Format.COMPRESSEDCOLUMNS)
                .setElementType(ChunkDescription.ElementType.BOOL).build();

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
        assertEquals(ChunkDescription.Format.COMPRESSEDCOLUMNS,
                PrivateAccessor.getField(topleft, "format"));

        assertEquals(0, topleft.getStartRow());
        assertEquals(0, topleft.getStartCol());

        assertEquals(15, topleft.getEndRow());
        assertEquals(10, topleft.getEndCol());

        assertEquals("bottom", bottom.getChunkId());
        assertEquals(ChunkDescription.Format.DENSE,
                PrivateAccessor.getField(bottom, "format"));

        assertEquals(0, topright.getStartRow());
        assertEquals(10, topright.getStartCol());

        assertEquals(15, topright.getEndRow());
        assertEquals(20, topright.getEndCol());

        assertEquals(ChunkDescription.Format.DENSE,
                PrivateAccessor.getField(topright, "format"));
        assertEquals("topright", topright.getChunkId());

        assertEquals(15, bottom.getStartRow());
        assertEquals(0, bottom.getStartCol());

        assertEquals(30, bottom.getEndRow());
        assertEquals(20, bottom.getEndCol());
    }
}
