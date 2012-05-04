package it.unipr.aotlab.dmat.core.net.messages;

import it.unipr.aotlab.dmat.core.generated.MatrixPieceListWire.MatrixPiece;
import it.unipr.aotlab.dmat.core.generated.SendMatrixPieceListWire.SendMatrixPiece;
import it.unipr.aotlab.dmat.core.net.MessageOrder;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

import java.io.IOException;

public abstract class Operation extends MessageOrder {
    public abstract void exec(NodeState nodeState) throws IOException;

    public abstract int nofMissingPieces();
    public abstract MatrixPiece missingPiece(int index);

    public abstract int nofPiacesAwaitingUpdate();
    public abstract MatrixPiece awaitingUpdate(int index);

    public abstract int nofPiecesToBeSent();
    public abstract SendMatrixPiece pieceToBeSent(int index);
}
