/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * ClientConnection class created for each new connection to server.  Contains
 * DelayedPriorityBlockingQueues outBound and inBound which are used to push
 * data to be written to or read from the connection.  Relevent infomration such
 * as username and IP address are also stored here.
 *
 */

package Challenge268EASY_server;

import java.net.Socket;
import java.net.InetAddress;

public class ClientConnection {
    public Socket connectionSocket;
    protected String userName = "";
    public final int connectionID;
    public DelayedPriorityBlockingQueue<String> outBound;
    public DelayedPriorityBlockingQueue<String> inBound;
    protected String ipAddrString;
    protected boolean connectionOnline = false;
    protected boolean proc = false;
    
    public ClientConnection(Socket s, int conID) {
        connectionSocket = s;
        connectionID = conID;
        outBound = new DelayedPriorityBlockingQueue<>();
        inBound = new DelayedPriorityBlockingQueue<>();
        ipAddrString = connectionSocket.getInetAddress().getHostAddress();
    }
    
    public void setUserName(String un) {
        userName = un;
    }
    
    public void goUp() {
        connectionOnline = true;
    }
    
    public void goDown() {
        connectionOnline = false;
    }
    
    public boolean online() {
        return connectionOnline;
    }
    
    public void lockConnection() {
        proc = true;
    }
    
    public void unlockConnection() {
        proc = false;
    }
    
    public boolean needsProcessing() {
        if((inBound.size() != 0) && !this.proc) return true;  // it would be nice if this here accounted for CON in queue
        return false;
    }
    
    public String getIPAddr() {
        return ipAddrString;
    }
    
    public String getUsername() {
        return userName;
    }
    
    public String userAtAddr() {
        return userName + "@" + getIPAddr();
    }
}
