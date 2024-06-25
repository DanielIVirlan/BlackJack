package model;

import java.util.stream.IntStream;

/**
 * Class representing a playing card.
 * Each card has a suit, rank, and a value.
 */
public class Card {
    public String suit; // The suit of the card (e.g., "Hearts", "Diamonds", etc.)
    public String rank; // The rank of the card (e.g., "Ace", "King", "Queen", etc.)
    public int value;   // The numerical value of the card in the game

    /**
     * Constructs a card with specified suit, rank, and value.
     * @param suit The suit of the card (e.g., "Hearts", "Diamonds", etc.).
     * @param rank The rank of the card (e.g., "Ace", "King", "Queen", etc.).
     * @param value The numerical value of the card in the game (e.g., 2-10, or special values for face cards).
     */
    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    /**
     * Retrieves the suit of the card.
     * @return The suit of the card.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Retrieves the rank of the card.
     * @return The rank of the card.
     */
    public String getRank() {
        return rank;
    }

    /**
     * Retrieves the numerical value of the card.
     * @return The value of the card.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns a string representation of the card.
     * The format is "rank-suit" (e.g., "Ace-Hearts").
     * @return The string representation of the card.
     */
    @Override
    public String toString() {
        return rank + "-" + suit;
    }

    /**
     * Retrieves the value of an Ace card in the game.
     * Aces can have a value of either 1 or 11, depending on other cards in play.
     * @return The value of the Ace card (either 1 or 11).
     */
    public int getValueOfAce() {
        // If the rank is Ace, Jack, Queen, or King, return 10; otherwise, parse rank to integer
        return IntStream.of(10, 10, 10, 11)
                .filter(val -> rank.equals("A") || rank.equals("J") || rank.equals("Q") || rank.equals("K"))
                .findFirst()
                .orElse(Integer.parseInt(rank));
    }

    /**
     * Checks if the card is an Ace.
     * @return true if the card is an Ace, false otherwise.
     */
    public boolean isAce() {
        return rank.equals("A");
    }

    /**
     * Retrieves the file path of the image representing the card.
     * The image file path is assumed to be under "cards/" directory and named "rank-suit.png".
     * @return The file path of the card image.
     */
    public String getImagePath() {
        return "cards/" + toString() + ".png";
    }
}
