package it.unipr.aotlab.dmat.core.net;

public interface Message {
    String contentType();

    byte[] content();
}
