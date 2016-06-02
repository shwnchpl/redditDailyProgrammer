/*
 * Code by Shawn M. Chapla, 02 June 2016.
 *
 * A simple deck of cards class. Shuffles on initialization by
 * randomly swapping one hundred cards. Cards are removed when dealt.
 *
 */
 
package Challenge268INTR_server;

import java.util.ArrayList;
import java.util.Random;

public class DeckOfCards {
    ArrayList<BlackjackCard> deck;
    
    public DeckOfCards() {
        deck = new ArrayList<BlackjackCard>(52);
        
        for(int st = 0; st <= 3; st++)
            for(int fce = 0; fce <= 12; fce++)
                deck.add(new BlackjackCard(fce, st));
        
        this.shuffle();
    }
    
    public void shuffle() {
        this.shuffle(100);
    }
    
    public void shuffle(int quant) {
        Random rand = new Random();
        int deckIndi = deck.size() - 1;
        
        for(int i = 0; i < quant; i++) {   // give 'em a good shuffle
            BlackjackCard temp; 
            
            int indexA = rand.nextInt(deckIndi);
            int indexB = rand.nextInt(deckIndi);
            
            temp = deck.get(indexB);
            deck.set(indexB, deck.get(indexA));
            deck.set(indexA, temp);
        }
    }
    
    public int length() {
        return deck.size();
    }
    
    public BlackjackCard deal() {
        return deck.remove(0);
    }
}
