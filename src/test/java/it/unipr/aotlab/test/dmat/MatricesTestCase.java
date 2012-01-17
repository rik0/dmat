package it.unipr.aotlab.test.dmat;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;

import org.junit.*;
import static junit.framework.Assert.*;

public class MatricesTestCase {
    
    @Test
    public void buildMatrix() throws ChunkNotFound {
            Matrices a = new Matrices();
            Matrix c;

            c = a.setNofColumns(20)
                    .setNofRows(30)
                    .splitHorizzontalyChuck(null, 15, "top", "bottom")
                    .splitVerticallyChuck("top", 10, "topleft", "topright")
                    .setChunkFormat("topleft",
                            ChunkDescription.Format.COMPRESSEDCOLUMNS)
                    .setElementType(ChunkDescription.ElementType.BOOL)
                    .build();

            Chunk topleft = c.getChunk("topleft");
            Chunk topright = c.getChunk("topright");
            Chunk bottom = c.getChunk("bottom");

            assertEquals(0, topleft.getStartRow());
            assertEquals(0, topleft.getStartCol());

            assertEquals(15, topleft.getEndRow());            
            assertEquals(10, topleft.getEndCol());

            assertEquals(0, topright.getStartRow());
            assertEquals(10, topright.getStartCol());

            assertEquals(15, topright.getEndRow());            
            assertEquals(20, topright.getEndCol());
            
            assertEquals(15, bottom.getStartRow());
            assertEquals(0, bottom.getStartCol());

            assertEquals(30, bottom.getEndRow());            
            assertEquals(20, bottom.getEndCol());
    }
}
