package it.unipr.aotlab.dmat.core.net;

import java.io.IOException;

public interface DeliveryManager {
    Message getNextDelivery() throws Exception;

    void close() throws IOException;

    void initialize() throws IOException;
}
