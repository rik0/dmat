package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.net.zeroMQ.MessageSender;

import org.zeromq.ZMQ;

public class MainNode {
    static public void showUsage() {
        System.out
                .println("Use: node.jar nodeName masterName");
        System.out.println();
    }

    static public int realMain(String[] args) throws Exception {
        if (args.length < 2) {
            showUsage();
            throw new BadQuit("node.jar expects two parameters.");
        }

        ZMQ.Context context = ZMQ.context(1);
        MessageSender messageSender = new MessageSender(context);
        WorkingNode wn = new WorkingNode(args[0], args[1], messageSender);
        try {
            System.err.println("Started node: " + args[0]);
            wn.consumerLoop();
        }
        finally {
            context.term();
        }

        return 0;
    }

    static public class BadQuit extends Error {
        private static final long serialVersionUID = -7374469760174497184L;

        public BadQuit(String message) {
            super(message);
        }
    }

    static public class Quit extends Error {
        private static final long serialVersionUID = 7604051811503970597L;
    }

    static public int mainReturnValue = 0;

    static public int intMain(String[] args) {
        try {
            mainReturnValue = realMain(args);
        } catch (Quit e) {
        } catch (BadQuit e) {
            mainReturnValue += 1;
            System.err.println("Fatal Error: " + e.getMessage());
        } catch (Error e) {
            mainReturnValue = 1;
            e.printStackTrace();
            System.err.println("Caught Error.\n "
                    + e.getClass()
                    + ". Message: "
                    + e.getMessage());

        } catch (Exception e) {
            mainReturnValue = 2;
            e.printStackTrace();
            System.err.println("Caught Exception.\n "
                    + e.getClass()
                    + ". Message: "
                    + e.getMessage());

        } catch (Throwable e) {
            mainReturnValue = 3;
            e.printStackTrace();
            System.err.println("Caught Throwable.\n "
                    + e.getClass()
                    + ". Message: "
                    + e.getMessage());
        }

        return mainReturnValue;
    }

    static public void main(String[] args) {
        System.exit(intMain(args));
    }
}
