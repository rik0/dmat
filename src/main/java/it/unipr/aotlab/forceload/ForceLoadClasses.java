package it.unipr.aotlab.forceload;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ForceLoadClasses {
    static public void listFromFile(Class<?> c, String filename) {
        try {
            ForceLoadClasses.listFromFileImpl(filename);
        } catch (ClassNotFoundException e) {
            throw new DMatInternalError(
                    c.getCanonicalName() + " initialization invalid file. Original Message "
                            + e.getMessage());

        } catch (IOException e) {
            throw new DMatInternalError(
                    c.getCanonicalName() + " initialization IO error. Original Message "
                            + e.getMessage());

        }
        
    }
    static public void listFromFileImpl(String filename)
            throws ClassNotFoundException, IOException {
        InputStream classListStream = null;
        try {
            classListStream = ForceLoadClasses.class.getClassLoader()
                    .getResourceAsStream("forceload/" + filename);
            if (classListStream == null)
                throw new IOException("Could not read " + filename);

            DataInputStream classListDataInputStream = new DataInputStream(
                    classListStream);
            InputStreamReader classListReader = new InputStreamReader(
                    classListDataInputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(classListReader);

            String className;
            while ((className = reader.readLine()) != null) {
                Class.forName(className);
            }
        } catch (UnsupportedEncodingException e) {
            // Unsupported UTF-8 means a bug in the JVM!
            // http://docs.oracle.com/javase/1.4.2/docs/api/java/nio/charset/Charset.html
            e.printStackTrace();
            System.exit(1);
        } finally {
            classListStream.close();
        }
    }
}
