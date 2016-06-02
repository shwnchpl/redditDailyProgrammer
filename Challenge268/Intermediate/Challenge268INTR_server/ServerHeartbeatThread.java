/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * Sends heartbeat to each client and drops them if they fail to respond.
 * Heartbeat and response are both plaintext.
 *
 */

package Challenge268INTR_server;

import java.util.Timer;
import java.util.TimerTask;

public class ServerHeartbeatThread extends Thread {
    private ClientConnection connection;
    private SynchronizedConnectionList connections;
    private DelayedPriorityBlockingQueue<String> messages;
    private static final short TIME_OUT = 5000;
    private boolean abort = false;
    private Timer timer;
    
    private TimerTask timeOut = new TimerTask() {
        public void run() {
            dropConnection("Dropping client " + connection.userAtAddr()
                + " due to no heartbeat.");
        }
    };
    
    public ServerHeartbeatThread(ClientConnection c,
            SynchronizedConnectionList cs, DelayedPriorityBlockingQueue<String> m) {
        super("ServerHeartbeatThread");
        connection = c;
        connections = cs;
        messages = m;
        timer = new Timer();
    }
    
    private void dropConnection(String dropMSG) {
            messages.delayAdd(dropMSG);
            connections.remove(connection);
            abort = true;
    }
    
    public void run() {
        try {
            connection.outBound.delayAdd("CONCHECK");
            
            timer.schedule(timeOut, TIME_OUT);
            
            while(!connection.inBound.contains("CON")) {
                if(abort) return;
                try {
                    Thread.sleep(TIME_OUT / 10); // sleep for a tenth of the time_out period
                } catch (InterruptedException e) { } // this won't happen
            }
            timer.cancel();
            
            connection.inBound.remove("CON");
            
            messages.delayAdd("Recieved heartbeat from " + connection.userAtAddr());
        } catch (Exception e) {
            if(abort) return;
            timer.cancel();
            messages.delayAdd("Caught an exception while checking for heartbeat from Client "
                            + connection.userAtAddr());
            dropConnection("Dropping client at " + connection.userAtAddr() + " due to exception.");
        }
    }
}
