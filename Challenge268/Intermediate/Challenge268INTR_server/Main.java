/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * Main thread.  Starts the ServerMainThread, ServerListenThread, and ServerBroadcastThreads.
 * Also establishes a DelayedPriorityBlockingQueue for console messages and prints from that
 * queue when they appear.
 *
 * Possible future implementation plans for the whole program:
 *  - add a console input thread with commands such as "kick" to remove users and 
 *  "quit" to bring down the server.
 *  - design a more nuanced message protocol
 *  - add timestamps to heartbeat messages
 *
 */

package Challenge268INTR_server;

public class Main {
    public static void main(String[] args) throws Exception {
        SynchronizedConnectionList connections = new SynchronizedConnectionList();
        DelayedPriorityBlockingQueue<String> messageQueue = new DelayedPriorityBlockingQueue<>();
        String recievedMSG = "";
        final short port = 3003;
        
        
        (new ServerMainThread(connections, messageQueue)).start();
        (new ServerListenThread(connections, messageQueue, port)).start();
        (new ServerBroadcastThread(messageQueue)).start();
        
        System.out.println("Server up and running.");
        
        while((recievedMSG = messageQueue.take()) != "GODOWN")  // This shouldn't ever actually throw an exception.
            System.out.println(recievedMSG);
        
        System.exit(0);
    }
}
