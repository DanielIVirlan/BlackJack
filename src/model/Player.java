package model;

import java.util.ArrayList;

/**
 * The Player class represents a player in a card game.
 * It manages the player's hand of cards, calculates the sum of card values,
 * and keeps track of the number of aces in the hand.
 */
public class Player {
	public ArrayList<Card> hand; // The player's hand of cards
	public int sum;              // The sum of values of cards in the hand
	public int aceCount;         // The count of aces in the hand

    /**
     * Constructs a new Player object with an empty hand, sum initialized to 0,
     * and ace count initialized to 0.
     */
    public Player() {
        this.hand = new ArrayList<>();
        this.sum = 0;
        this.aceCount = 0;
    }

    /**
     * Adds a card to the player's hand and updates the sum of card values and ace count.
     *
     * @param card The card to be added to the player's hand.
     */
    public void addToHand(Card card) {
        hand.add(card);
        updateSumAndAceCount();
    }

    /**
     * Updates the sum of card values and the count of aces in the player's hand.
     * This method should be called whenever a card is added to the hand.
     */
    private void updateSumAndAceCount() {
        sum = hand.stream().mapToInt(Card::getValue).sum(); // Calculate sum of card values
        aceCount = (int) hand.stream().filter(Card::isAce).count(); // Count number of aces
    }

    /**
     * Returns the current sum of card values in the player's hand.
     *
     * @return The sum of card values in the hand.
     */
    public int getSum() {
        return sum;
    }

    /**
     * Reduces the sum of card values by 10 for each ace in the hand,
     * if reducing the sum helps to stay below or equal to 21.
     *
     * @return The reduced sum of card values.
     */
    public int reduceAce() {
        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }
        return sum;
    }

    /**
     * Returns the player's current hand of cards.
     *
     * @return The ArrayList of cards representing the player's hand.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Returns the number of aces in the player's hand.
     *
     * @return The count of aces in the hand.
     */
    public int getAceCount() {
        return aceCount;
    }
}
