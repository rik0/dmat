package it.unipr.aotlab.test.dmat;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.loaders.MatrixMarket;
import it.unipr.aotlab.dmat.core.matrixPiece.Int32Triplet;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.junit.Test;

public class MatrixMarketTestCase {
    @Test
    public void testMatrixMarketGeneralInteger() {
        StringBuffer sb = new StringBuffer();

        sb.append("%%MatrixMarket Matrix coordiNAte iNTeger geneRAL\n");
        sb.append("%\n");
        sb.append("% Comment!\n");
        sb.append("\n");
        sb.append("% and empty lines!\n");
        sb.append("          \n");
        sb.append("  % With ugly spaces leading and trailing   \n");
        sb.append("\n");
        sb.append("    5     6     8\n");
        sb.append("    1     1   1\n");
        sb.append("  2      2   4\n");
        sb.append("    3    3   189\n");
        sb.append("1     4   -1\n");
        sb.append("    2     4   6\n");
        sb.append("\n");
        sb.append("    4     4           8     \n");
        sb.append("\n");
        sb.append("% still a comment\n");
        sb.append("    4     5   3\n");
        sb.append("    5     5   1\n");
        sb.append("\n");
        String marketFileIntegerGeneral = sb.toString();

        int[][] triplets = {
                {0, 0, 1},
                {1, 1, 4},
                {2, 2, 189},
                {0, 3, -1},
                {1, 3, 6},
                {3, 3, 8},
                {3, 4, 3},
                {4, 4, 1},
        };

        InputStream is = new ByteArrayInputStream(marketFileIntegerGeneral.getBytes());

        try {
            MatrixMarket mm = new MatrixMarket(is);

            assertEquals(mm.getNofRows(), 5);
            assertEquals(mm.getNofCols(), 6);
            assertEquals(mm.getNofElements(), 8);

            Iterator<Triplet> mmi = mm.getIterator();
            int index = 0;
            while (mmi.hasNext()) {
                Int32Triplet t = (Int32Triplet) mmi.next();

                assertEquals(triplets[index][0], t.row);
                assertEquals(triplets[index][1], t.col);
                assertEquals(triplets[index][2], t.value);

                ++index;
            }

        } catch (IOException e) {
            assertTrue(false);
        } catch (DMatError e) {
            assertTrue(false);
        } catch (ClassCastException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testMatrixMarketSkewSymmetricInteger() {
        StringBuffer sb = new StringBuffer();

        sb.append("%%MatrixMarket matrix cOOrdinate intEger skew-symmetric\n");
        sb.append("%\n");
        sb.append("% Comment!\n");
        sb.append("\n");
        sb.append("% and empty lines!\n");
        sb.append("          \n");
        sb.append("  % With ugly spaces leading and trailing   \n");
        sb.append("\n");
        sb.append("    6     6     8\n");
        sb.append("    2     1   1\n");
        sb.append("  3      2   4\n");
        sb.append("    4    3   189\n");
        sb.append(" 3     2   -1\n");
        sb.append("    5     4   6\n");
        sb.append("\n");
        sb.append("    6     4           8     \n");
        sb.append("\n");
        sb.append("% still a comment\n");
        sb.append("    6     5   3\n");
        sb.append("    6     1   1\n");
        sb.append("\n");
        String marketFileIntegerGeneral = sb.toString();

        int[][] triplets = {
                {1,0,1},
                {0,1,-1},
                {2,1,4},
                {1,2,-4},
                {3,2,189},
                {2,3,-189},
                {2,1,-1},
                {1,2,1},
                {4,3,6},
                {3,4,-6},
                {5,3,8},
                {3,5,-8},
                {5,4,3},
                {4,5,-3},
                {5,0,1},
                {0,5,-1},
        };

        InputStream is = new ByteArrayInputStream(marketFileIntegerGeneral.getBytes());

        try {
            MatrixMarket mm = new MatrixMarket(is);

            assertEquals(mm.getNofRows(), 6);
            assertEquals(mm.getNofCols(), 6);
            assertEquals(mm.getNofElements(), 8);

            Iterator<Triplet> mmi = mm.getIterator();
            int index = 0;
            while (mmi.hasNext()) {
                Int32Triplet t = (Int32Triplet) mmi.next();

                assertEquals(triplets[index][0], t.row);
                assertEquals(triplets[index][1], t.col);
                assertEquals(triplets[index][2], t.value);

                ++index;
            }

        } catch (IOException e) {
            assertTrue(false);
        } catch (DMatError e) {
            assertTrue(false);
        } catch (ClassCastException e) {
            assertTrue(false);
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

}
