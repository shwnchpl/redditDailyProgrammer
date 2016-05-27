/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * ServerRequestProcessorThread processes commands from clients. Manages a processing
 * lock on ClientConnections. In the event of disaster, clears a client's inBound queue
 * and unlocks them so as to spare their connection.
 *
 */

package Challenge268EASY_server;

public class ServerRequestProcessorThread extends Thread {
    private SynchronizedConnectionList connections;
    private ClientConnection processingClient;
    private DelayedPriorityBlockingQueue<String> messages;
    
    public ServerRequestProcessorThread(ClientConnection c,
            SynchronizedConnectionList cs, DelayedPriorityBlockingQueue<String> m) {
        super("ServerRequestProcessorThread");
        processingClient = c;
        connections = cs;
        messages = m;
    }
    
    private void dropConnection(String dropMSG) {
            for(ClientConnection cli_con : connections)
                cli_con.outBound.delayAdd(processingClient.getUsername() + " has logged off.");
            messages.delayAdd(dropMSG);
            connections.remove(processingClient);
    }
    
    private void listUsers() {
        for(String un : connections.usernameSet())
            processingClient.outBound.delayAdd(un);
    }
    
    private void sendAll(String command) {
        String message = processingClient.getUsername() + ": " + command.substring(8);
        
        messages.delayAdd("User message to all:\n{" + message + "}");
        
        for(ClientConnection cli_con : connections)
            cli_con.outBound.delayAdd(message);
    }
    
    private void sendUser(String toUser, String command) {
        ClientConnection targetUser; 
        
        if((targetUser = connections.getByUsername(toUser)) != null) {
            String clientUN = processingClient.getUsername();
            String message = "(PM) " + clientUN + ": "
                                + command.substring(10 + toUser.length()); // trim it up
            
            processingClient.outBound.delayAdd(message);
            targetUser.outBound.delayAdd(message);
            messages.delayAdd("Private message from " + clientUN + " to " + toUser
                                + ":\n{" + message + "}");
        } else {
            processingClient.outBound.delayAdd("Invalid user.");
        }
    }
    
    // here we handle all of the commands from clients
    @Override
    public void run() {
        processingClient.lockConnection();       // prevent a second processing thread from starting
        try {
            for(String command : processingClient.inBound) {
                String[] commandArray = command.split("\\s+");
                switch(commandArray[0]) {
                    case "CON":
                        continue;  // we ignore the con signal
                    case "quit":
                        dropConnection("Dropping connection to "
                            + processingClient.userAtAddr() + " in response to 'quit' request.");
                        return;
                    case "sendall":
                        if(commandArray.length >= 2) sendAll(command);
                        else processingClient.outBound.delayAdd("Usage: sendall <message>");
                        break;
                    case "senduser":
                        if(commandArray.length >= 3) sendUser(commandArray[1], command);
                        else processingClient.outBound.delayAdd("Usage: senduser <user> <message>");
                        break;
                    case "listusers":
                        listUsers();
                        break;
                    default:
                        processingClient.outBound.delayAdd("Command not found.");
                }
                processingClient.inBound.remove(command);
            }
        } catch (Exception e) {
            // it's highly unlikely that we should catch any kind of exception
            // but if we do, we don't want it to happen again
            processingClient.inBound.clear();
        } finally {
            // and even if we do, we have to unlock the connection so it can be serviced again later
            processingClient.unlockConnection();
        }
    }
}
