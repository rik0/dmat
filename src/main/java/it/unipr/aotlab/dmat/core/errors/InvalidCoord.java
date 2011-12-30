package it.unipr.aotlab.dmat.core.errors;

public class InvalidCoord extends DMatUncheckedException {
    private static final long serialVersionUID = 178526261941350254L;

    public InvalidCoord() {
        super();
    }

    public InvalidCoord(String message) {
        super(message);
    }
}
