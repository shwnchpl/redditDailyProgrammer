/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * ConnectionListenThread is a simple thread called after a connection has
 * been accepted-- there is one running for each connection.  The thread reads
 * input from the client and adds it to the connection's inBound queue.
 *
 */

package Challenge268EASY_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ConnectionListenThread extends Thread {
    private ClientConnection listenConnection;
    private DelayedPriorityBlockingQueue<String> messages;
    

    public ConnectionListenThread(ClientConnection c) {
        super("ConnectionListenThread");
        listenConnection = c;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
            new InputStreamReader(
                listenConnection.connectionSocket.getInputStream()));
            
            while(listenConnection.online()) {
                String readStr;
                if((readStr = in.readLine()) == null) {
                    listenConnection.inBound.delayAdd("quit");
                    break;
                } else {
                    listenConnection.inBound.delayAdd(readStr);
                }
            }
        } catch (Exception e) {  // probably should handle these exceptions better
            if(!listenConnection.online()) return; // Our connection has gone offline-- time to go.
            messages.delayAdd("Caught Exception while listening to " + listenConnection.userAtAddr()
                                    + "\nProcessing as 'quit' command.");
            listenConnection.inBound.delayAdd("quit");  // something has gone wrong; boot 'em
        }
    }
}
