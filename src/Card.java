/**
 * Class representing a playing card.
 */
public class Card {
    private String value;
    private String type;

    /**
     * Constructor to initialize a card with given value and type.
     * @param value the value of the card (e.g., "A", "2", "K")
     * @param type the type or suit of the card (e.g., "H", "D", "C", "S")
     */
    public Card(String value, String type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Returns a string representation of the card.
     * @return the string representation of the card (e.g., "A-H" for Ace of Hearts)
     */
    @Override
    public String toString() {
        return value + "-" + type;
    }

    /**
     * Retrieves the numeric value of the card.
     * @return the numeric value of the card (11 for Ace, 10 for J, Q, K, otherwise numeric value)
     */
    public int getValue() {
        if ("AJQK".contains(value)) {
            if (value.equals("A")) {
                return 11;
            }
            return 10;
        }
        return Integer.parseInt(value);
    }

    /**
     * Checks if the card is an Ace.
     * @return true if the card is an Ace, false otherwise
     */
    public boolean isAce() {
        return value.equals("A");
    }

    /**
     * Retrieves the file path of the card's image.
     * @return the file path to the card's image
     */
    public String getImagePath() {
        return "cards/" + toString() + ".png";
    }
}

