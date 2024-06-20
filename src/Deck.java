import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Deck class represents a standard deck of 52 playing cards.
 * It provides methods to build, shuffle, and draw cards from the deck.
 */
public class Deck {
    private List<Card> deck;
    private Random random;

    /**
     * Constructs a new Deck object, initializes the deck, builds it with 52 cards,
     * and shuffles the deck.
     */
    public Deck() {
        this.deck = new ArrayList<>();
        this.random = new Random();
        buildDeck();
        shuffleDeck();
    }

    /**
     * Builds the deck with 52 cards, one of each combination of the four types
     * (Hearts, Diamonds, Clubs, Spades) and the thirteen values (Ace, 2-10, Jack, Queen, King).
     */
    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] types = {"H", "D", "C", "S"};
        String[] values = {"A","2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("BUILD DECK:");
        System.out.println(deck);
    }

    /**
     * Shuffles the deck using a random number generator to swap each card with
     * another randomly chosen card in the deck.
     */
    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            // Get a random card j from the deck
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            // Swap the current card i with the random card j
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }

        System.out.println("AFTER SHUFFLE :");
        System.out.println(deck);
    }

    /**
     * Draws a card from the deck. The drawn card is removed from the deck.
     *
     * @return the drawn card from the deck.
     */
    public Card drawCard() {
        return deck.remove(deck.size() - 1);
    }
}
