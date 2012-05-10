package it.unipr.aotlab.dmat.core.util;

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
}
