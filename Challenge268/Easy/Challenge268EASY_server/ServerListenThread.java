/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * Listens for new connections and forwards them on to a ServerAcceptThread as they come in.
 *
 */

package Challenge268EASY_server;

import java.net.ServerSocket;
import java.io.IOException;

public class ServerListenThread extends Thread {
    private SynchronizedConnectionList connections;
    private DelayedPriorityBlockingQueue<String> messages;
    private final short port;
    static int signonCount = 0;
    
    public ServerListenThread(SynchronizedConnectionList c,
            DelayedPriorityBlockingQueue<String> m, short p) {
        super("ServerListenThread");
        connections = c;
        messages = m;
        port = p;
    }
    
    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while(true)
                (new AcceptConnectionThread(connections, new ClientConnection(serverSocket.accept(), ++signonCount),
                        messages)).start();
         } catch(IOException e) {
                messages.delayAdd("Error: could not listen on port " + port);
                messages.delayAdd("GODOWN");
        }
    }
}
