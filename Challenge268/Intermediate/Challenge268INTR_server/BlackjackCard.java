/*
 * Code by Shawn M. Chapla, 02 June 2016.
 *
 * A simple blackjack card class.  Face and suit are both stored as ints.
 *
 */

package Challenge268INTR_server;

public class BlackjackCard {
    public static final String[] cardFace = {"Ace", "Two", "Three", "Four",
                                            "Five", "Six", "Seven", "Eight",
                                            "Nine", "Ten", "Jack", "Queen", "King"};
    public static final String[] cardSuit = {"Hearts", "Clubs", "Spades", "Diamonds"};
    
    private int face, suit;
    
    public BlackjackCard(int f, int s) {
        face = f;
        suit = s;
    }
    
    public int cardValue() {
        if(face < 10) return face + 1;
        else return 10;
    }
    
    public String getFace() {
        return cardFace[face];
    }
    
    public String getSuit() {
        return cardSuit[suit];
    }
    
    public String toString() {
        return cardFace[face] + " of " + cardSuit[suit];
    }
}
