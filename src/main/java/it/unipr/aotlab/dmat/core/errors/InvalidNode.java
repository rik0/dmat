package it.unipr.aotlab.dmat.core.errors;

public class InvalidNode extends DMatUncheckedException {
    private static final long serialVersionUID = 5145256142260126731L;

    public InvalidNode() {
        super();
    }

    public InvalidNode(String message) {
        super(message);
    }
}
