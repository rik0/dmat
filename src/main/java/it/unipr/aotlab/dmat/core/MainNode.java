package it.unipr.aotlab.dmat.core;

import it.unipr.aotlab.dmat.core.net.rabbitMQ.Address;
import it.unipr.aotlab.dmat.core.net.rabbitMQ.Connector;

public class MainNode {

    static public void showUsage() {
        System.out
                .println("Use: node.jar nodeName brokerName brokerAddress [brokerPort]");
        System.out.println();
    }

    static public Address buildBrokerAddress(final String[] args) {
        Address address = null;
        if (args.length > 3) {
            address = new Address(args[2], Integer.parseInt(args[3]));
        } else {
            address = new Address(args[2]);
        }
        return address;
    }

    static public int realMain(final String[] args) {
        if (args.length < 3) {
            showUsage();
            throw new BadQuit("node.jar expects at least three parameters.");
        }
        final Connector rabbitMQConnector = new Connector(buildBrokerAddress(args));
        final WorkingNode wn = new WorkingNode(args[0], args[1], rabbitMQConnector);

        wn.consumerLoop();

        return 0;
    }

    static public class BadQuit extends Error {
        public BadQuit(final String message) {
            super(message);
        }
    }

    static public class Quit extends Error {
    }

    static public int mainReturnValue = 0;

    static public int intMain(final String[] args) {
        try {
            mainReturnValue = realMain(args);
        } catch (final Quit e) {
        } catch (final BadQuit e) {
            mainReturnValue += 1;
            System.err.println("Fatal Error: " + e.getMessage());
        } catch (final Error e) {
            mainReturnValue = 1;
            e.printStackTrace();
            System.err.println("Caught Error.\nClass: " + e.getClass()
                    + ". Message: " + e.getMessage());

        } catch (final Exception e) {
            mainReturnValue = 2;
            e.printStackTrace();
            System.err.println("Caught Exception.\nClass: " + e.getClass()
                    + ". Message: " + e.getMessage());

        } catch (final Throwable e) {
            mainReturnValue = 3;
            e.printStackTrace();
            System.err.println("Caught Throwable.\nClass: " + e.getClass()
                    + ". Message: " + e.getMessage());
        }
        return mainReturnValue;
    }

    static public void main(final String[] args) {
        System.exit(intMain(args));
    }
}
