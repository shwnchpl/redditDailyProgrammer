/*
 * Code by Shawn M. Chapla, 02 June 2016.
 *
 * ServerRequestProcessorThread processes commands from clients. Manages a processing
 * lock on ClientConnections. In the event of disaster, clears a client's inBound queue
 * and unlocks them so as to spare their connection. ServerRequestProcessorThread shares
 * access to a BlackjackGameEnvironment class so that it is able to error handle for
 * blackjack related inputs on a per-connection basis.
 *
 */

package Challenge268INTR_server;

public class ServerRequestProcessorThread extends Thread {
    private SynchronizedConnectionList connections;
    private ClientConnection processingClient;
    private DelayedPriorityBlockingQueue<String> messages;
    private BlackjackGameEnvironment blackjackGameEnv;
    
    public ServerRequestProcessorThread(ClientConnection c,
										SynchronizedConnectionList cs,
										DelayedPriorityBlockingQueue<String> m,
										BlackjackGameEnvironment bje) {
        super("ServerRequestProcessorThread");
        processingClient = c;
        connections = cs;
        messages = m;
        blackjackGameEnv = bje;
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
    
    private void sendBlackjackCommand(String command) {   // MUST already be in protocol by the time sending here
		blackjackGameEnv.blackjackCommands.delayAdd(command
						+ " " + processingClient.getUsername());
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
                    case "blackjack":
						if(blackjackGameEnv.getGameState() != GameStateEnum.NO_GAME)
							processingClient.outBound.delayAdd("You can't start a game! There's already a game in progress.");
						else
							(new ServerBlackjackThread(messages, connections, blackjackGameEnv, processingClient.getUsername())).start();
						break;
					case "join":
						switch(blackjackGameEnv.getGameState()) {
							case NO_GAME:
								processingClient.outBound.delayAdd(
									"There's no game to join right now. Type 'blackjack' to start a game.");
								break;
							case IN_PROGRESS:
								processingClient.outBound.delayAdd(
									"It's took late to join the current game.");
								break;
							case WAITING_JOIN:
								String pcun;
								if(blackjackGameEnv.getPlayers().contains((pcun = processingClient.getUsername())))
									processingClient.outBound.delayAdd(
										"You can't join the game. You're already in it!");
								else
									sendBlackjackCommand("JOIN");
								break;
						}
						break;
					case "start":
						switch(blackjackGameEnv.getGameState()) {
							case NO_GAME:
								processingClient.outBound.delayAdd(
									"There's no game to start right now. Type 'blackjack' to start a game.");
								break;
							case IN_PROGRESS:
								processingClient.outBound.delayAdd(
									"The game is already in progress.");
								break;
							case WAITING_JOIN:
								String pcun;
								String firp;
								if(!(firp = blackjackGameEnv.getFirstPlayer()).equals((pcun = processingClient.getUsername())))
									processingClient.outBound.delayAdd(
										"It isn't your game to start! Wait for " + firp + " to start the game.");
								else if(blackjackGameEnv.getPlayers().size() <= 1)
									processingClient.outBound.delayAdd(
										"Wait for more players to start the game. You can't play by yourself!");
								else
									sendBlackjackCommand("START");
								break;
						}
						break;
					case "take":
					case "pass":
						switch(blackjackGameEnv.getGameState()) {
							case NO_GAME:
							case WAITING_JOIN:
							case COMPLETE:
								processingClient.outBound.delayAdd(
										"You can't do that right now!");
								break;
							case IN_PROGRESS:
								String unt;
								
								if(!processingClient.getUsername().equals((unt = blackjackGameEnv.getCurrentPlayer())))
									processingClient.outBound.delayAdd(
												"It's " + unt + "'s turn, not yours!");
								else
									sendBlackjackCommand(commandArray[0]);
								break;
						}
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
