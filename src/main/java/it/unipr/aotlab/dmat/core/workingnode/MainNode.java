package it.unipr.aotlab.dmat.core.workingnode;

import gnu.getopt.Getopt;
import it.unipr.aotlab.dmat.core.net.IPAddress;
import it.unipr.aotlab.dmat.core.net.zeroMQ.MessageSender;

import org.zeromq.ZMQ;

public class MainNode {
    static public String defaultBroadcastAddress = "239.255.255.255";
    static public String defaultBroadcastPort = "43981";
    static public String defaultSyncPort = "43982";

    static public class Options {
        String nodeName = null;
        String listeningPort = null;
        String masterName = null;
        String broadcastAddress = null;
        String broadcastPort = null;
        String syncPort = null;
        int zmqThreads = -1;

        public boolean validate() {
            boolean result = true;
            if (nodeName == null) {
                System.err.println("Missing node name!");
                result = false;
            }

            if (listeningPort == null) {
                System.err.println("Missing listening port!");
                result = false;
            }

            if (masterName == null) {
                System.err.println("Missing master name!");
                result = false;
            }

            if (broadcastAddress == null)
                broadcastAddress = defaultBroadcastAddress;

            if (broadcastPort == null)
                broadcastPort = defaultBroadcastPort;

            if (syncPort == null)
                syncPort = defaultSyncPort;

            if (zmqThreads < 1)
                zmqThreads = 1;

            return result;
        }
    }

    public static Options readOptions(String[] args) {
        Options result = new Options();
        int c;
        Getopt g = new Getopt("node", args, "n:p:m:b:d:s:t:h");
        g.setOpterr(false);

        while ((c = g.getopt()) != -1) {
            switch (c) {
            case 'n':
                result.nodeName = g.getOptarg();
                break;

            case 'p':
                result.listeningPort = g.getOptarg();
                break;

            case 'm':
                result.masterName = g.getOptarg();
                break;

            case 'b':
                result.broadcastAddress = g.getOptarg();
                break;

            case 'd':
                result.broadcastPort = g.getOptarg();
                break;

            case 's':
                result.syncPort = g.getOptarg();
                break;

            case 't':
                result.zmqThreads = Integer.parseInt(g.getOptarg());
                break;

            case 'h':
                showUsage();
                throw new Quit();
            default:
                showUsage();
                throw new BadQuit("Unknown commandline option.");
            }
        }

        if (! result.validate())
            throw new BadQuit("Wrong commandline options, try -h for help.");

        return result;
    }

    static public void showUsage() {
        System.out.println("Use summary. All options without default are required.");
        System.out.println();
        System.out.println(" -n node name,");
        System.out.println(" -p listening port,");
        System.out.println(" -m master name,");
        System.out.println(" -b broadcast address (default: " + defaultBroadcastAddress + "),");
        System.out.println(" -d broadcast port (default: " + defaultBroadcastPort + "),");
        System.out.println(" -s syncroniziation port (default: " + defaultSyncPort + ").");
        System.out.println(" -t number of threads for zmq (default: 1).");
        System.out.println();
    }

    static public int realMain(String[] args) throws Exception {
        Options options = readOptions(args);
        ZMQ.Context context = ZMQ.context(options.zmqThreads);
        MessageSender messageSender = new MessageSender(context,
                    options.nodeName,
                    new IPAddress(options.broadcastAddress, Integer.parseInt(options.broadcastPort)),
                    options.syncPort);
        WorkingNode wn = new WorkingNode(options.nodeName,
                                         options.masterName,
                                         messageSender,
                                         context,
                                         options.listeningPort);

        try {
            System.err.println("Started node: " + options.nodeName);
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
