/*
 * Code by Shawn M. Chapla, 27 May 2016.
 * 
 * An AcceptConnectionThread is started any time a new connection is recieved and
 * needs to be accepted.  The thread handles a simple handshake in which the client
 * requests a username and ensures the username is available before granting it.
 * AcceptConnectionThread concludes by adding the estabished connection to the main
 * list of connections and starting listen and write threads for the connection.
 *
 */

package Challenge268INTR_server;

import java.util.Timer;
import java.util.TimerTask;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class AcceptConnectionThread extends Thread {
    private SynchronizedConnectionList connections;
    private ClientConnection acceptConnection;
    private DelayedPriorityBlockingQueue<String> messages;
    private static final short NEW_CONNECTION_TIME_OUT = 15000; // 15 second timeout on UN entry
    private static final short LOGIN_ATTEMPT_LIMIT = 3; // limit 3 login attempts
    private Boolean abort = false;
    private Timer timer;
    
    public AcceptConnectionThread(SynchronizedConnectionList cs,
        ClientConnection c, DelayedPriorityBlockingQueue<String> m) {
        super("AcceptConnectionThread");
        connections = cs;
        acceptConnection = c;
        messages = m;
        timer = new Timer();
    }
    
    private void dropConnection(String reasonSTR, PrintWriter out) { // this function is slighty questionable
        messages.delayAdd("Dropping connection to " +
            acceptConnection.getIPAddr() + ": " + reasonSTR);
        try {
            if(out != null) out.println("SCT");   // There may be a better way to handle this.
            acceptConnection.connectionSocket.close();
        } catch (IOException e) {
            // No matter, we're on the way down anyway.
        } finally {
            abort = true;
        }
    }

    // simple overload to handle the fact that in the event of
    // exception, there will be no Prinstream to pass
    private void dropConnection(String reasonSTR) {
        messages.delayAdd("Dropping connection to " +
            acceptConnection.getIPAddr() + ": " + reasonSTR);
        try {
            acceptConnection.connectionSocket.close();
        } catch (IOException e) {
            // No matter, we're on the way down anyway.
        } finally {
            abort = true;
        }
    }

    public void run() {
        String userName = "";
        String[] unArr;
        try {
            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                        acceptConnection.connectionSocket.getInputStream()));
            PrintWriter out = new PrintWriter(
                                acceptConnection.connectionSocket.getOutputStream(), true);
            
            TimerTask timeOut = new TimerTask() {
                public void run() {
                    dropConnection("TIMEOUT", out);
                }
            };
            
            out.println("GRT");
            
            timer.schedule(timeOut, NEW_CONNECTION_TIME_OUT);
            
            for(int attempts = 1;
                    ((unArr = in.readLine().split("\\s+")).length < 1) ||
                    !connections.userNameFree((userName = unArr[0]));
                            attempts++) {
                if(attempts >= LOGIN_ATTEMPT_LIMIT) dropConnection("ATTEMPTS", out);
                out.println("UNNF");
            }

            timer.cancel();
        } catch (Exception e) {
            timer.cancel();
            if(abort) return;           // Who doesn't love a little spaghetti?
            messages.delayAdd("Caught exception while trying to estabish connection. Dropping.");
            dropConnection("EXCEPTION"); // good chance we're already down, so
        } finally {
            if(abort) return;  // we shouldn't make it here but just in case
        }
        
        acceptConnection.setUserName(userName);
        
        messages.delayAdd("Recieved connection from " + acceptConnection.getIPAddr() +
            ". Assigning ID " + acceptConnection.connectionID);
        messages.delayAdd("ID " + acceptConnection.connectionID + " has chosen username: " + userName);
        acceptConnection.goUp();
        
        acceptConnection.outBound.delayAdd("WLK");              // put the WLK welcome signal into queue
        
        for(ClientConnection cli_con : connections)             // notify everyone of the logon
            cli_con.outBound.delayAdd(userName + " has logged on.");
        
        connections.add(acceptConnection);                      // add connection to the main list
        (new ConnectionListenThread(acceptConnection)).start(); // fire up the listen and write threads
        (new ConnectionWriteThread(acceptConnection)).start();
    }
}
