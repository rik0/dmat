package it.unipr.aotlab.dmat.mains;

import it.unipr.aotlab.dmat.core.errors.ChunkNotFound;
import it.unipr.aotlab.dmat.core.generated.ChunkDescription;
import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Matrix;

public class BuildAMatrix {
    @SuppressWarnings("unused")
    public static void main(String[] argv) {
        try {
            Matrices a = new Matrices();
            Matrix c;

            c = a.setNofColumns(20)
                    .setNofRows(20)
                    .splitHorizzontalyChuck(null, 10, "top", "bottom")
                    .splitVerticallyChuck("top", 10, "topleft", "topright")
                    .setChunkFormat("topleft",
                            ChunkDescription.Format.COMPRESSEDCOLUMNS)
                    .setElementType(ChunkDescription.ElementType.BOOL)
                    .build();

            int b = 0;
        } catch (ChunkNotFound e) {
            e.printStackTrace();
        }

    }
}
