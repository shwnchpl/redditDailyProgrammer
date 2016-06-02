/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * ConnectionWriteThread is a simple thread called after a connection has been
 * accepted.  There is always one write thread running for each connection.  The
 * thread takes anything that is in the ClientConnection's outBound queue and sends
 * it out to the client.
 *
 */

package Challenge268INTR_server;

import java.io.PrintWriter;

public class ConnectionWriteThread extends Thread {
    private ClientConnection writeConnection;
    private DelayedPriorityBlockingQueue<String> messages;

    public ConnectionWriteThread(ClientConnection c) {
        super("ConnectionWriteThread");
        writeConnection = c;
    }
    
    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(
                writeConnection.connectionSocket.getOutputStream(), true);
            
            while(writeConnection.online()) {
                out.println(writeConnection.outBound.take());
            }
        } catch (Exception e) {  // ideally this would handle an IOException and an Inturrupt seperately
            if(!writeConnection.online()) return; // already offine-- time to bail
            messages.delayAdd("Caught exception while attempt to write to "
                + writeConnection.userAtAddr() + "\nProcessing as 'quit' command.");
            writeConnection.inBound.delayAdd("quit");
        }
    }
}
