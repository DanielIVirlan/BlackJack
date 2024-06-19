import java.util.*;

/**
 * The GameLogic class manages the game mechanics of a Blackjack-like card game.
 * It handles the deck, players (dealer, player1, bot1, bot2), game statistics,
 * and game flow including player actions and determining the winner.
 */
public class GameLogic {
    private Deck deck;           // The deck of cards used in the game
    private Player dealer;       // The dealer player
    private Player player1;      // Player 1
    private Player bot1;         // Bot player 1
    private Player bot2;         // Bot player 2

    public static int gamesPlayed = 0;  // Total number of games played
    public static int gamesWon = 0;     // Number of games won
    public static int gamesLost = 0;    // Number of games lost

    private Card hiddenCard;     // The hidden card for the dealer
    @SuppressWarnings("unused")
    private Random random = new Random();  // Random number generator

    
    
    
    /**
     * Constructs a GameLogic object and starts a new game by initializing the deck
     * and dealing initial cards to players.
     * 
     */
 
    public GameLogic() {
        startGame();
    }

    /**
     * Initializes a new game by creating a new deck and dealing initial cards to
     * the dealer, player1, bot1, and bot2.
     */
    public void startGame() {
        
        deck = new Deck();
        dealer = new Player();
        player1 = new Player();
        bot1 = new Player();
        bot2 = new Player();

        hiddenCard = deck.drawCard();
        dealer.addToHand(hiddenCard);
        dealer.addToHand(deck.drawCard());

        for (int i = 0; i < 2; i++) {
            player1.addToHand(deck.drawCard());
            bot1.addToHand(deck.drawCard());
            bot2.addToHand(deck.drawCard());
        }
    }

    /**
     * Handles the action when player1 decides to hit (draw another card).
     */
    public void playerHit() {
        player1.addToHand(deck.drawCard());
    }

    /**
     * Simulates the dealer's turn to draw cards until their sum is 17 or more.
     */
    public void dealerTurn() {
        while (dealer.getSum() < 17) {
            dealer.addToHand(deck.drawCard());
        }
    }

    /**
     * Simulates bot1's turn to draw cards until their sum is 17 or more.
     */
    public void bot1Turn() {
        while (bot1.getSum() < 17) {
            bot1.addToHand(deck.drawCard());
        }
    }

    /**
     * Simulates bot2's turn to draw cards until their sum is 17 or more.
     */
    public void bot2Turn() {
        while (bot2.getSum() < 17) {
            bot2.addToHand(deck.drawCard());
        }
    }

    /**
     * Determines the winner of the game based on the current sums of all players
     * and the dealer.
     *
     * @return The name of the winner ("Dealer", "Player", "Bot 1", or "Bot 2").
     */
    public String determineWinner() {
        String winner = "Dealer";

        if (player1.getSum() <= 21) {
            winner = JBlackJack.playerName;
            gamesWon++;
        } else {
            gamesLost++;
        }

        if (bot1.getSum() <= 21 && bot1.getSum() > player1.getSum()) {
            winner = "Bot 1";
        }

        if (bot2.getSum() <= 21 && bot2.getSum() > Math.max(player1.getSum(), bot1.getSum())) {
            winner = "Bot 2";
        }

        if (dealer.getSum() <= 21 && dealer.getSum() > Math.max(player1.getSum(), Math.max(bot1.getSum(), bot2.getSum()))) {
            winner = "Dealer";
        }

        return winner;
    }
    /**
 * Gets the dealer of the blackjack game.
 * @return The dealer player
 */
    public Player getDealer() {
        return dealer;
    }

    /**
     * Gets the first player of the blackjack game.
     * @return Player 1
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Gets the first bot player in the game.
     * @return Bot 1
     */
    public Player getBot1() {
        return bot1;
    }

    /**
     * Gets the second bot player in the game.
     * @return Bot 2
     */
    public Player getBot2() {
        return bot2;
    }

    /**
     * Gets the hidden card used in the game.
     * @return The hidden card
     */
    public Card getHiddenCard() {
        return hiddenCard;
    }
}




