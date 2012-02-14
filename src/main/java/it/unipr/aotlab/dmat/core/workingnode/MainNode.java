package it.unipr.aotlab.dmat.core.workingnode;

import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.MessageSender;

public class MainNode {
    static public void showUsage() {
        System.out
                .println("Use: node.jar nodeName brokerName brokerAddress [brokerPort]");
        System.out.println();
    }

    static public Address buildBrokerAddress(String[] args) {
        Address address = null;
        if (args.length > 3) {
            address = new Address(args[2], Integer.parseInt(args[3]));
        } else {
            address = new Address(args[2]);
        }
        return address;
    }

    static public int realMain(String[] args) throws Exception {
        if (args.length < 3) {
            showUsage();
            throw new BadQuit("node.jar expects at least three parameters.");
        }

        MessageSender rabbitMQConnector = new MessageSender(new Connector(buildBrokerAddress(args)));
        WorkingNode wn = new WorkingNode(args[0], args[1], rabbitMQConnector);

        try {
            wn.consumerLoop();
        }
        finally {
            MessageSender.closeConnection();
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
