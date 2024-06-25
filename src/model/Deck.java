package model;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a deck of playing cards.
 * The deck can contain multiple decks combined together.
 */
public class Deck {
	public Stack<Card> cards; // Stack to hold the cards in the deck

    /**
     * Constructs a deck with a specified number of standard decks combined together.
     * @param numDecks The number of standard decks to combine in this deck.
     */
    public Deck(int numDecks) {
        cards = new Stack<>();
        buildDeck(numDecks); // Builds the deck with specified number of decks
        Collections.shuffle(cards); // Shuffle the deck after building
    }

    /**
     * Builds the deck by adding cards from multiple standard decks.
     * @param numDecks The number of standard decks to combine in this deck.
     */
    public void buildDeck(int numDecks) {
        // Define the standard suits, ranks, and values of cards
        String[] suits = {"H", "D", "C", "S"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

        // Generate cards for the specified number of decks
        List<Card> newCards = IntStream.range(0, numDecks)
                .boxed()
                .flatMap(n -> IntStream.range(0, suits.length)
                        .boxed()
                        .flatMap(i -> IntStream.range(0, ranks.length)
                                .mapToObj(j -> new Card(suits[i], ranks[j], values[j]))
                        )
                )
                .collect(Collectors.toList());

        // Add all generated cards to the deck
        cards.addAll(newCards);
    }

    /**
     * Draws (removes and returns) a card from the top of the deck.
     * @return The card drawn from the deck.
     */
    public Card drawCard() {
        return cards.pop(); // Remove and return the top card from the deck
    }
}
