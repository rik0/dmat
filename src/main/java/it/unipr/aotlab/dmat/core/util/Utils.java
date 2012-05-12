package it.unipr.aotlab.dmat.core.util;

import it.unipr.aotlab.dmat.core.errors.DMatInternalError;

import org.zeromq.ZMQ;

public class Utils {
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public static void awaitThreadDeath(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
        }
    }

    public static void zmqSend(ZMQ.Socket socket, byte[] message) {
        if (socket.send(message, 0) == false) {
            throw new DMatInternalError("zmq socket failed sending.");
        }
    }

    public static void zmqSendMore(ZMQ.Socket socket, byte[] message) {
        if (socket.send(message, ZMQ.SNDMORE) == false) {
            throw new DMatInternalError("zmq socket failed sending.");
        }
    }
}
