import java.util.ArrayList;

/**
 * The Player class represents a player in a card game.
 * It manages the player's hand of cards, calculates the sum of card values,
 * and keeps track of the number of aces in the hand.
 */
public class Player {
    private ArrayList<Card> hand; // The player's hand of cards
    private int sum;              // The sum of values of cards in the hand
    private int aceCount;         // The count of aces in the hand

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
        sum += card.getValue();
        aceCount += card.isAce() ? 1 : 0;
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
        int currentSum = getSum();
        int currentAceCount = this.aceCount;
        while (currentSum > 21 && currentAceCount > 0) {
            currentSum -= 10;
            currentAceCount--;
        }
        return currentSum;
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

