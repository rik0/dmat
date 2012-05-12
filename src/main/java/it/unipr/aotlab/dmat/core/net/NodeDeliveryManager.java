package it.unipr.aotlab.dmat.core.net;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;
import it.unipr.aotlab.dmat.core.generated.EnvelopedMessageWire.EnvelopedMessageBody;
import it.unipr.aotlab.dmat.core.net.Message.MessageKind;
import it.unipr.aotlab.dmat.core.workingnode.NodeState;

public abstract class NodeDeliveryManager extends DeliveryManager {
    NodeState currentState;

    public abstract void prepareForReceivingMulticast(String signature);

    public NodeDeliveryManager(NodeState currentState) {
        this.currentState = currentState;
    }

    @Override
    protected boolean isCorrectMessage(EnvelopedMessageBody possibleDelivery) {
        int serialNo = possibleDelivery.getSerialNo();
        int messageKind = possibleDelivery.getMessageKind();
        boolean executingOrder = currentState.busyExecutingOrder();
        int workingOnSerialNo = currentState.getCurrentSerialNo();

        if ((messageKind & ~7) != 0)
            throw new DMatInternalError("Unexpected message kind!");

        if (messageKind == MessageKind.IMMEDIATE.tag)
            return true;

        if (messageKind == MessageKind.ORDER.tag
                && serialNo == workingOnSerialNo
                && !executingOrder)
            return true;

        if (messageKind == MessageKind.SUPPORT.tag
                && serialNo == workingOnSerialNo
                && executingOrder)
            return true;

        return false;
    }
}
