/*
 * Code by Shawn M. Chapla, 02 June 2016.
 *
 * A simple class to store some data related to the current blackjack game
 * so that it can be shared between the ServerBlackjackThread and
 * ServerRequestProcessorThreads. This allows processor threads to control
 * blackjack related commands so that the blackjack thread only gets
 * appropriate information.
 *
 */


package Challenge268INTR_server;

import java.util.ArrayList;

class BlackjackGameEnvironment {
    public DelayedPriorityBlockingQueue<String> blackjackCommands;
    protected GameStateEnum currentGameState;
    protected String firstPlayer;
    protected ArrayList<String> players;
    protected int turn;                      // storing the turn as an integer allows the use of simple arrays
                                             // to store the score and who is still playing
    
    public BlackjackGameEnvironment() {
        blackjackCommands = new DelayedPriorityBlockingQueue<>();
        firstPlayer = "";
        players = new ArrayList<>();
        currentGameState = GameStateEnum.NO_GAME;
        turn = 0;
    }
    
    public void panic() {
        blackjackCommands.delayAdd("PANIC");  // used in the event of an unexpected logoff
    }
    
    public void clear() {                     // clears out the environment; called every time a game ends
        blackjackCommands = new DelayedPriorityBlockingQueue<>();
        firstPlayer = "";
        players = new ArrayList<>();
        currentGameState = GameStateEnum.NO_GAME;
        turn = 0;
    }
    
    public void advanceTurn() {
        if(turn < players.size() - 1) turn++;
        else turn = 0;
    }
    
    public int getCurrentTurn() {
        return turn;
    }
    
    public int numberOfPlayers() {
        return players.size();
    }
    
    public String getCurrentPlayer() {
        return players.get(turn);
    }
    
    public synchronized void setFirstPlayer(String fp) {
        firstPlayer = fp;
    }
    
    public synchronized String getFirstPlayer() {
        return firstPlayer;
    } 
    
    public synchronized void addPlayer(String name) {
        players.add(name);
    }
    
    public synchronized ArrayList<String> getPlayers() {
        return players;
    }
    
    public synchronized void setGameState(GameStateEnum gse) {
        currentGameState = gse;
    }
    
    public synchronized GameStateEnum getGameState() {
        return currentGameState;
    }
}
