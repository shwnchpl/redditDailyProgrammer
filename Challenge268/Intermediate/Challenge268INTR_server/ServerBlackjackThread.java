/*
 * Code by Shawn M. Chapla, 02 June 2016.
 *
 * ServerBlackjackThread is started when a user enters the 'blackjack' command
 * (provided there isn't already a blackjack game running). Waits for players
 * to join the game and begins either when the initial player sends 'start'
 * or when eight players have joined. Receives error-checked commands from
 * processor threads and proceeds with the game accordingly. Quits if a user
 * at turn has logged off.
 *
 */
 
package Challenge268INTR_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ServerBlackjackThread extends Thread {
    private DelayedPriorityBlockingQueue<String> consoleMessages;
    private SynchronizedConnectionList connections;
    private BlackjackGameEnvironment blackjackGameEnv;
    private HashMap<String, ArrayList<BlackjackCard>> hands;
    private DeckOfCards cards;
    private boolean[] stillPlaying;
    private int[] score;
    private static final short PRESENT_CONF_PERIOD = 3000;
    private static final short PLAYER_LIMIT = 8;     // it is mathematically impossible for 8 players to
                                                     // use the whole deck of cards in blackjack.  Changing this number
                                                     // could lead to problems as the code does not currently handle for
                                                     // the deck running out of cards (since it never will with this limit in place)
    
    public ServerBlackjackThread(DelayedPriorityBlockingQueue<String> cm,
                                    SynchronizedConnectionList cn,
                                    BlackjackGameEnvironment bge,
                                    String fp) {
        super("ServerBlackjackThread");
        consoleMessages = cm;
        connections = cn;
        blackjackGameEnv = bge;
        
        blackjackGameEnv.setFirstPlayer(fp);
        blackjackGameEnv.addPlayer(fp);
        blackjackGameEnv.setGameState(GameStateEnum.WAITING_JOIN);
    }
    
    // sends a message to all connected users
    private void sendAll(String message) {        
        consoleMessages.delayAdd("Blackjack message to all:\n{" + message + "}");
        
        for(ClientConnection cli_con : connections)
            cli_con.outBound.delayAdd(message);
    }

    // calculate the value of a hand; returns 0 for hands that are bust
    private static int handValue(ArrayList<BlackjackCard> hand) {
        int value = 0;
        int ace_count = 0;
        
        for(BlackjackCard c : hand) {
            int cv;
            
            if((cv = c.cardValue()) != 1) value += cv;
            else ace_count++;
        }
        
        while(ace_count > 0) {
            if((value + 11 + ((ace_count - 1) * 1)) <= 21) value += 11;
            else value += 1;
            ace_count--;
        }
        
        if(value <= 21) return value;
        else return 0;
    }

    // print a hand out as a string
    private static String handString(ArrayList<BlackjackCard> hand) {
        String ret_str = "";
        
        for(BlackjackCard c : hand) {
            if(ret_str.length() != 0) ret_str += ", ";
            ret_str += c.toString();
        }
        
        return ret_str;
    }
    
    // gets called after everyone has joined and it's time to start the game
    private void initiateGame() {
        blackjackGameEnv.setGameState(GameStateEnum.IN_PROGRESS);
        
        cards = new DeckOfCards();
        hands = new HashMap<>();
        
        int numOfPlayers = blackjackGameEnv.numberOfPlayers();
        
        score = new int[numOfPlayers];
        stillPlaying = new boolean[numOfPlayers];
        
        for(int i = 0; i < numOfPlayers; i++) {
            String playr = blackjackGameEnv.getPlayers().get(i);
        
            ArrayList<BlackjackCard> plr_crds = new ArrayList<>(5);
            
            plr_crds.add(cards.deal());
            plr_crds.add(cards.deal());
            
            hands.put(playr, plr_crds);
            
            stillPlaying[i] = true;
            score[i] = handValue(plr_crds);
            
            sendAll(playr + " has been dealt " + handString(plr_crds));
        }
    }
    
    // sets the current turn to the next valid player; returns
    // false if there are no more valid players
    private boolean passTurnNextValid() {
        blackjackGameEnv.advanceTurn();
        
        int firstAttempt = blackjackGameEnv.getCurrentTurn();
        int prospectiveNext = firstAttempt;
        
        do {
            if(stillPlaying[prospectiveNext]) return true;
             blackjackGameEnv.advanceTurn();
            prospectiveNext =  blackjackGameEnv.getCurrentTurn();
        } while(prospectiveNext != firstAttempt);
        
        return false; // no one is still playing, time to end the game
    }

    // prompts the next player "take or pass"
    private void sendNextPrompt() {
        ClientConnection cpcc;
        String cpun;
                    
        if((cpcc = connections.getByUsername(
                    (cpun = blackjackGameEnv.getCurrentPlayer()))) != null)
            cpcc.outBound.delayAdd("(PM) " + cpun + ", take or pass?");
        else {
            blackjackGameEnv.panic(); 			    // it looks like we've had a log-out event
        }                                           // time to panic
    }

    // announces the winner(s)
    private void announceWinner() {
        int max_score = 0;
        ArrayList<String> winners = new ArrayList<>(2);
        
        for(int i = 0; i < score.length; i++)
            if(score[i] > max_score) max_score = score[i];
        for(int i = 0; i < score.length; i++)
            if(score[i] == max_score) winners.add(blackjackGameEnv.getPlayers().get(i));
        
        int winsz;
        if((winsz = winners.size()) == 1) {
            String wun = winners.get(0);
            sendAll("The winner is " + wun + " with " + handString(hands.get(wun)) + "!");
        } else {
            String sendStr = "It looks like we have a " + winsz + "-way draw between:";
            
            for(String wun : winners)
                sendStr += "\n" + wun + " with " + handString(hands.get(wun));
            
            sendAll(sendStr);
        }
    }
    
    @Override
    public void run() {
        // start a game specific heartbeat here?
        
        sendAll(blackjackGameEnv.getFirstPlayer() + " has started a game of blackjack."
                        + " Type 'join' to join.");
        
        while(blackjackGameEnv.getGameState() != GameStateEnum.COMPLETE) {
            String[] currComArr = null;
            
            TimerTask checkBadLogout = new TimerTask() {
                public void run() {
                if(connections.getByUsername(blackjackGameEnv.getCurrentPlayer()) == null) // we've had a logout
                    blackjackGameEnv.panic();  // this will send an inturrupt by nullifying blackjackCommands
                }
            };
            
            Timer logoutCheckTimer = new Timer();
            
            try {
                logoutCheckTimer.schedule(checkBadLogout, 0, PRESENT_CONF_PERIOD);  // check every three seconds to make sure the person
                currComArr = blackjackGameEnv.blackjackCommands                      // we expect input from will be sending us input
                                                    .take().split("\\s+");
                logoutCheckTimer.cancel();
            } catch (Exception e) {
                consoleMessages.delayAdd("Caught an exception while waiting for Blackjack command. Failing quietly.");
                logoutCheckTimer.cancel(); // just in case
                blackjackGameEnv.clear();
                return;
            }
            
            switch(currComArr[0]) {  // JOIN USERNAME
                case "JOIN":  // PROCESSOR MUST NOT SEND INAPPROPRIATE JOIN; might be worth considering a player limit
                    blackjackGameEnv.addPlayer(currComArr[1]);
                    sendAll(currComArr[1] + " has joined "
                                    + blackjackGameEnv.getFirstPlayer() + "'s blackjack game.");
                    
                    if(blackjackGameEnv.numberOfPlayers() >= PLAYER_LIMIT) {
                        sendAll("Player limit reached!  Starting the game!");
                        initiateGame();
                        sendNextPrompt();
                    }
                    break;
                case "START": // server must make sure 
                    sendAll(blackjackGameEnv.getFirstPlayer() + " has started the game!");
                    initiateGame();
                    
                    sendNextPrompt();
                    break;
                case "take":
                    BlackjackCard drawnCard;
                    ArrayList<BlackjackCard> playerHand;
                    String currentPlayer = blackjackGameEnv.getCurrentPlayer();
                    int currentTurn = blackjackGameEnv.getCurrentTurn();
                    
                    (playerHand = hands.get(currentPlayer)).add((drawnCard = cards.deal()));
                    sendAll(currentPlayer + " takes a " + drawnCard.toString());
                    
                    if((score[currentTurn] = handValue(playerHand)) == 0) {
                        stillPlaying[currentTurn] = false;
                        sendAll(currentPlayer + " has gone bust.");
                    }
                    
                    if(!passTurnNextValid()) {
                        blackjackGameEnv.setGameState(GameStateEnum.COMPLETE);
                        break;
                    }
                    
                    sendNextPrompt();
                    break;
                case "pass":
                    stillPlaying[blackjackGameEnv.getCurrentTurn()] = false;
                    
                    sendAll(blackjackGameEnv.getCurrentPlayer() + " has passed!");
                    
                    if(!passTurnNextValid()) {
                        blackjackGameEnv.setGameState(GameStateEnum.COMPLETE);
                        break;
                    }
                    
                    sendNextPrompt();
                    break;
                case "PANIC":
					blackjackGameEnv.setGameState(GameStateEnum.COMPLETE);
                    sendAll("It appears that " + blackjackGameEnv.getCurrentPlayer() + " has logged out. Game cancelled.");
                    blackjackGameEnv.clear();
                    return;
            }
        }
        
        announceWinner();
        
        blackjackGameEnv.clear();
    }
}
