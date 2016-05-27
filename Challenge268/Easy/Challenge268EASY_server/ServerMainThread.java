/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * MainServerThread. Periodically starts HeartbeatThreads and also starts
 * processing threads for connections that have unresolved data in their
 * inBound queues.
 *
 */

package Challenge268EASY_server;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Iterator;

public class ServerMainThread extends Thread {
    private SynchronizedConnectionList connections;
    private DelayedPriorityBlockingQueue<String> messages;
    private static final short SERVER_TIMEOUT = 20000;
    
    private TimerTask timeOut = new TimerTask() {
        public void run() {
            checkConnectionStatus();
        }
    };
    
    public ServerMainThread(SynchronizedConnectionList c,
                                DelayedPriorityBlockingQueue<String> m) {
        super("ServerMainThread");
        connections = c;
        messages = m;
    }
    
    private void checkConnectionStatus() {
        Iterator<ClientConnection> con_itr = connections.iterator();
        
        while(con_itr.hasNext()) {
            (new ServerHeartbeatThread(con_itr.next(), connections, messages)).start();
        }
    }
    
    private void processRequests() {
        for(ClientConnection cli_con : connections)
            if(cli_con.needsProcessing())
                (new ServerRequestProcessorThread(cli_con, connections, messages)).start();
    }
    
    @Override
    public void run() {
        Timer toTimer = new Timer();
        toTimer.schedule(timeOut, 0, SERVER_TIMEOUT);
        
        while(true) {
            processRequests();
            
            try { 
                Thread.sleep(10);  // hardcoded latency
            } catch (Exception e) { 
                messages.delayAdd("Caught an exception in MainServerThread.");
            } // highly unlikely
        }
    }
}
