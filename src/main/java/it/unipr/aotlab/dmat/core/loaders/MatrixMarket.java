package it.unipr.aotlab.dmat.core.loaders;

import it.unipr.aotlab.dmat.core.errors.DMatError;
import it.unipr.aotlab.dmat.core.matrixPiece.Triplet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.regex.Pattern;

public class MatrixMarket implements Loader {
    private BufferedReader inputStream;

    private int rows;
    private int cols;
    private int nofElements;

    private String format;
    private String field;
    private String symmetry;

    private static Pattern spaces = Pattern.compile(" *");
    private static Pattern leadingNTrailingSpaces = Pattern.compile("^ *\\| *$");

    public MatrixMarket(InputStream inputStream) {
        this.inputStream =  new BufferedReader
                           (new InputStreamReader
                           (new DataInputStream(inputStream)));
    }

    private void findAndReadHeader() throws IOException, DMatError {
        String strLine;
        String[] headers = null;

        while ((strLine = inputStream.readLine()) != null) {
            strLine = leadingNTrailingSpaces.matcher(strLine).replaceAll("");

            if (strLine.startsWith("%%")) {
                headers = spaces.split(strLine);
                break;
            }
        }

        if (headers == null || headers.length != 5 || !headers[1].toLowerCase().equals("matrix"))
            throw new DMatError("Invalid Matrix Market File");

        format = headers[2].toLowerCase();
        field = headers[3].toLowerCase();
        symmetry = headers[4].toLowerCase();

        checkHeaders();
    }

    private void checkHeaders() throws DMatError {
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

    private void findAndReadMatrixSize() throws IOException, DMatError {
        String strLine = null;
        String[] values = null;

        while ((strLine = nextLine()) != null) {
            values = spaces.split(strLine);
            break;
        }

        if (values == null || values.length != 3)
            throw new DMatError("Invalid Matrix Market File");

        rows = Integer.parseInt(values[0]);
        cols = Integer.parseInt(values[1]);
        nofElements = Integer.parseInt(values[2]);

    }

    private String nextLine() throws IOException {
        String strLine = null;

        while ((strLine = inputStream.readLine()) != null) {
            strLine = leadingNTrailingSpaces.matcher(strLine).replaceAll("");

            if (!strLine.equals("") && !strLine.startsWith("%"))
                break;
        }

        return strLine;
    }

    @Override
    public Iterator<Triplet> getIterator() {
        return null;
    }

    private interface ReadLine {

    }
}
