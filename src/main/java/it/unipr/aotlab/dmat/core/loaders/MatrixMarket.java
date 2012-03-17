package it.unipr.aotlab.dmat.core.loaders;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.matrixPiece.Int32Triplet;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;
import it.unipr.aotlab.dmat.core.util.Assertion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class MatrixMarket implements Loader {
    private BufferedReader inputStream;

    private int rows;
    private int cols;
    private int nofElements;

    private String format;
    private String field;
    private String symmetry;

    private FieldInterpreter fieldInterpreter;
    private FormatInterpreter formatInterpreter;
    private SymmetryInterpreter symmetryInterpreter;

    private static Pattern spaces = Pattern.compile(" +");
    private static Pattern leadingNTrailingSpaces = Pattern.compile("^ +| +$");

    public MatrixMarket(InputStream inputStream) throws IOException, DMatError {
        this.inputStream =  new BufferedReader
                           (new InputStreamReader
                           (new DataInputStream(inputStream)));

        findAndReadHeader();
        formatInterpreter.readMatrixSizeLine(findAndGetMatrixSize());
    }

    private void findAndReadHeader() throws IOException, DMatError {
        String strLine;
        String[] headers = null;

        while ((strLine = inputStream.readLine()) != null) {
            strLine = leadingNTrailingSpaces.matcher(strLine).replaceAll("");

            if (strLine.startsWith("%%")) {
                strLine = strLine.toLowerCase();
                headers = spaces.split(strLine);
                break;
            }
        }

        if (headers == null
                || headers.length != 5
                || ! headers[0].equals("%%matrixmarket")
                || ! headers[1].equals("matrix"))
            throw new DMatError("Invalid Matrix Market File");

        format = headers[2];
        field = headers[3];
        symmetry = headers[4];
        headersIsOk();

        initializeFormatInterpreter();
        initializeFieldInterpreter();
        initializeSymmetryInterpreter();
    }

    private void headersIsOk() throws DMatError {
        if ((format.equals("coordinate") || format.equals("array"))
                && (field.equals("real") || field.equals("integer") || field.equals("complex"))
                && (symmetry.equals("general") || symmetry.equals("symmetric") || symmetry.equals("skew-symmetric")))
            return;

        if ((format.equals("coordinate") || format.equals("array"))
                && field.equals("complex")
                && symmetry.equals("hermitian"))
            return;

        if (format.equals("coordinate")
                && field.equals("pattern")
                && (field.equals("general") || field.equals("symmetric")))
            return;

        throw new DMatError("Invalid Matrix Market File");
    }

    private String[] findAndGetMatrixSize() throws IOException, DMatError {
        String[] values = null;

        if ((values = nextLine()) == null)
            throw new DMatError("Invalid Matrix Market File");

        return values;
    }

    private String[] nextLine() throws IOException {
        String strLine = null;

        while ((strLine = inputStream.readLine()) != null) {
            strLine = leadingNTrailingSpaces.matcher(strLine).replaceAll("");

            if ( ! (strLine.equals("") || strLine.startsWith("%")))
                break;
        }

        return strLine == null ? null : spaces.split(strLine);
    }

    private class MatrixMarketFileIterator implements Iterator<Triplet> {
        Triplet[] nextTriplets = null;
        int nextTripletsIndex = 0;
        int hasNext = -1;

        void findNext() throws IOException, DMatError {
            Assertion.isTrue(hasNext == -1, "");

            if (nextTriplets != null)
                seekNextOnMemory();
            if (hasNext == -1)
                seekNextOnFile();

            Assertion.isTrue(hasNext != -1, "");
        }

        void seekNextOnMemory() {
            Assertion.isTrue(hasNext == -1, "");

            ++nextTripletsIndex;
            if (nextTripletsIndex < nextTriplets.length)
                hasNext = 1;
        }

        void seekNextOnFile() throws IOException, DMatError {
            Assertion.isTrue(hasNext == -1, "");

            String[] line = nextLine();
            if (line == null) {
                hasNext = 0;
                return;
            }

            line = formatInterpreter.normalizeValuesLine(line);
            Triplet explicitTrip = fieldInterpreter.readValues(line);
            nextTriplets = symmetryInterpreter.getImplicitValues(explicitTrip);
            nextTripletsIndex = 0;

            hasNext = 1;
        }

        @Override
        public boolean hasNext() {
            if (hasNext == -1)
                try {
                    findNext();
                } catch (IOException e) {
                    throw new DMatInternalError("IOException: " + e.getMessage());
                } catch (DMatError e) {
                    throw new DMatInternalError("DMATError: " + e.getMessage());
                }

            return hasNext != 0;
        }

        @Override
        public Triplet next() {
            if (hasNext()) {
                hasNext = -1;
                return nextTriplets[nextTripletsIndex];
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public int getNofRows() {
        return rows;
    }

    @Override
    public int getNofCols() {
        return cols;
    }

    @Override
    public int getNofElements() {
        return nofElements;
    }

    @Override
    public Iterator<Triplet> getIterator() {
        return new MatrixMarketFileIterator();
    }

    private interface FormatInterpreter {
        public void readMatrixSizeLine(String[] matrixSizeLine) throws DMatError;
        public String[] normalizeValuesLine(String[] valuesLine) throws DMatError;
    }

    private void initializeFormatInterpreter() throws DMatError {
        if (format.equals("coordinate")) {
            formatInterpreter = new CoordinateFormat();
            return;
        }

        throw new DMatError("DMat cannot read Matrix Market files of format " + format);
    }

    private interface FieldInterpreter {
        public Triplet readValues(String[] valueLine) throws DMatError;
        public Object getAdditiveInverse(Object v);
        public Object getConjugate(Object v);
    }

    private void initializeFieldInterpreter() throws DMatError {
        if (field.equals("integer")) {
            fieldInterpreter = new IntegerField();
            return;
        }

        throw new DMatError("DMat cannot read Matrix Market files with field  " + field);
    }

    private interface SymmetryInterpreter {
        public Triplet[] getImplicitValues(Triplet explicitValue) throws DMatError;
    }

    private void initializeSymmetryInterpreter() throws DMatError {
        if (symmetry.equals("general")) {
            symmetryInterpreter = new GeneralSymmetry();
            return;
        }

        if (symmetry.equals("symmetric")) {
            symmetryInterpreter = new SymmetricSymmetry();
            return;
        }

        if (symmetry.equals("skew-symmetric")) {
            symmetryInterpreter = new SkewSymmetricSymmetry();
            return;
        }

        throw new DMatError("DMat cannot read Matrix Market files with symmetry type of" + symmetry);
    }

    private class CoordinateFormat implements FormatInterpreter {
        @Override
        public void readMatrixSizeLine(String[] matrixSizeLine) throws DMatError {
            if (matrixSizeLine.length != 3)
                throw new DMatError("Invalid Matrix Market File");

            rows = Integer.parseInt(matrixSizeLine[0]);
            cols = Integer.parseInt(matrixSizeLine[1]);
            nofElements = Integer.parseInt(matrixSizeLine[2]);
        }

        @Override
        public String[] normalizeValuesLine(String[] valuesLine) {
            return valuesLine;
        }
    }

    private class IntegerField implements FieldInterpreter {
        @Override
        public Triplet readValues(String[] valueLine) throws DMatError {
            if (valueLine.length != 3)
                throw new DMatError("Invalid Matrix Market File");

            int currentRow = Integer.parseInt(valueLine[0]) - 1;
            int currentCol = Integer.parseInt(valueLine[1]) - 1;
            int currentValue = Integer.parseInt(valueLine[2]);

            return new Int32Triplet(currentRow, currentCol, currentValue);
        }

        @Override
        public Object getAdditiveInverse(Object v) {
            Integer tv = (Integer)v;
            return -tv;
        }

        @Override
        public Object getConjugate(Object v) {
            return v;
        }
    }

    private class GeneralSymmetry implements SymmetryInterpreter {
        @Override
        public Triplet[] getImplicitValues(Triplet explicitValue) throws DMatError {
            return new Triplet[] { explicitValue };
        }
    }

    private class SymmetricSymmetry implements SymmetryInterpreter {
        @Override
        public Triplet[] getImplicitValues(Triplet explicitValue)
                throws DMatError {
            Triplet implicitValue = explicitValue.getCopy();

            implicitValue.setRow(explicitValue.col());
            implicitValue.setCol(explicitValue.row());

            return new Triplet[] { explicitValue, implicitValue };
        }

    }

    private class SkewSymmetricSymmetry implements SymmetryInterpreter {
        @Override
        public Triplet[] getImplicitValues(Triplet explicitValue)
                throws DMatError {
            Triplet implicitValue = explicitValue.getCopy();

            implicitValue.setRow(explicitValue.col());
            implicitValue.setCol(explicitValue.row());
            implicitValue.setValue(fieldInterpreter.getAdditiveInverse(implicitValue.value()));

            return new Triplet[] { explicitValue, implicitValue };
        }
    }
}
