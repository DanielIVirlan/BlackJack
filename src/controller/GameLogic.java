package controller;

import model.Player;
import view.GameMenu;
import view.GameGUI;
import model.Card;
import model.Deck;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * The Controller class manages the game logic for a Blackjack game,
 * coordinating interactions between the players (human and bots),
 * the deck of cards, and the game view.
 */
@SuppressWarnings("deprecation")
public class GameLogic extends Observable {

    public static Deck deck;       // The deck of cards
    public static Player dealer;   // The dealer player
    public static Player player1;  // The human player
    public static Player bot1;     // Bot player 1
    public static Player bot2;     // Bot player 2

    public static int gamesPlayed = 0;  // Number of games played
    public static int gamesWon = 0;     // Number of games won by the player
    public static int gamesLost = 0;    // Number of games lost by the player

    public  static Card hiddenCard;  // The hidden card of the dealer

    private static GameLogic instance;
    
    /**
     * Constructs a new Controller instance and starts the game.
     */
    
    private GameLogic() {
        startGame();
    }
    
 // Public method to provide access to the singleton instance
    public static synchronized GameLogic getInstance() {
        if (instance == null) {
            instance = new GameLogic();
        }
        return instance;
    }

    /**
     * Initializes the game by creating a new deck, dealing cards to the dealer and players,
     * and notifying observers (the game view) of the initial game state.
     */
    public void startGame() {
        deck = new Deck(4);  // Create a new deck with 4 sets of cards
        dealer = new Player();  // Initialize the dealer
        player1 = new Player(); // Initialize the human player
        bot1 = new Player();    // Initialize bot player 1
        bot2 = new Player();    // Initialize bot player 2

        hiddenCard = deck.drawCard();  // Draw the dealer's hidden card
        dealer.addToHand(hiddenCard);  // Add hidden card to dealer's hand
        dealer.addToHand(deck.drawCard());  // Draw another card for the dealer

        // Draw two cards each for the human player and bots
        for (int i = 0; i < 2; i++) {
            player1.addToHand(deck.drawCard());
            bot1.addToHand(deck.drawCard());
            bot2.addToHand(deck.drawCard());
        }

        setChanged();  // Indicate that the game state has changed
        notifyObservers();  // Notify the observers (game view)
    }

    /**
     * Handles the player's hit action by drawing a card and updating the game state.
     */
    public void playerHit() {
        player1.addToHand(deck.drawCard());  // Draw a card for the player

        // Check if the player has aces and needs to reduce their value to stay below 21
        if (player1.getAceCount() > 0 && player1.getSum() > 21) {
            player1.reduceAce();
        }
        System.out.println("Player hit: " + player1.getHand());
    }

    /**
     * Simulates the dealer's turn by drawing cards until the sum is 17 or higher.
     */
    public void dealerTurn() {
        while (dealer.getSum() < 17) {
            dealer.addToHand(deck.drawCard());

            // Check if the dealer has aces and needs to reduce their value to stay below 21
            if (dealer.getAceCount() > 0 && dealer.getSum() > 21) {
                dealer.reduceAce();
            }
        }
        setChanged();
        notifyObservers();

        System.out.println("Dealer turn complete: " + dealer.getHand());
    }

    /**
     * Simulates bot player 1's turn by drawing cards until the sum is 17 or higher.
     */
    public void bot1Turn() {
        while (bot1.getSum() < 17) {
            bot1.addToHand(deck.drawCard());

            // Check if bot player 1 has aces and needs to reduce their value to stay below 21
            if (bot1.getAceCount() > 0 && bot1.getSum() > 21) {
                bot1.reduceAce();
            }
        }
        System.out.println("Bot1 turn complete: " + bot1.getHand());
    }

    /**
     * Simulates bot player 2's turn by drawing cards until the sum is 17 or higher.
     */
    public void bot2Turn() {
        while (bot2.getSum() < 17) {
            bot2.addToHand(deck.drawCard());

            // Check if bot player 2 has aces and needs to reduce their value to stay below 21
            if (bot2.getAceCount() > 0 && bot2.getSum() > 21) {
                bot2.reduceAce();
            }
        }
        System.out.println("Bot2 turn complete: " + bot2.getHand());
    }

    /**
     * Determines the winner of the game based on the current sums of all players and the dealer.
     *
     * @return A string message indicating the result of the game.
     */
    public String determineWinner() {
        int playerSum = player1.getSum();
        int bot1Sum = bot1.getSum();
        int bot2Sum = bot2.getSum();
        int dealerSum = dealer.getSum();

        // Determine if each player (human and bots) wins, loses, or ties with the dealer
        boolean playerWins = playerSum <= 21 && (playerSum > dealerSum || dealerSum > 21);
        if (playerWins) {
            gamesWon++;
        } else {
            gamesLost++;
        }
        boolean bot1Wins = bot1Sum <= 21 && (bot1Sum > dealerSum || dealerSum > 21);
        boolean bot2Wins = bot2Sum <= 21 && (bot2Sum > dealerSum || dealerSum > 21);

        // Determine if there are ties between players and the dealer
        boolean tiePlayer = playerSum == dealerSum && playerSum <= 21;
        boolean tieBot1 = bot1Sum == dealerSum && bot1Sum <= 21;
        boolean tieBot2 = bot2Sum == dealerSum && bot2Sum <= 21;

        StringBuilder result = new StringBuilder();

        // Append messages indicating results for each player and dealer
        if (tiePlayer) {
            result.append(GameMenu.playerName).append(" has tied with the dealer. ");
        } else if (playerWins) {
            result.append(GameMenu.playerName).append(" wins. ");
        } else {
            result.append(GameMenu.playerName).append(" loses. ");
        }

        if (tieBot1) {
            result.append("Bot 1 has tied with the dealer. ");
        } else if (bot1Wins) {
            result.append("Bot 1 wins. ");
        } else {
            result.append("Bot 1 loses. ");
        }

        if (tieBot2) {
            result.append("Bot 2 has tied with the dealer. ");
        } else if (bot2Wins) {
            result.append("Bot 2 wins. ");
        } else {
            result.append("Bot 2 loses. ");
        }

        // Determine if the dealer wins or ties with all players
        boolean dealerWins = !tiePlayer && !tieBot1 && !tieBot2 &&
                !playerWins && !bot1Wins && !bot2Wins;

        if (dealerWins) {
            result.append("Dealer wins.");
        } else {
            if (!tiePlayer && !playerWins && !tieBot1 && !bot1Wins && !tieBot2 && !bot2Wins) {
                result.append("Dealer has tied with all players.");
            } else {
                result.append("Dealer loses.");
            }
        }

        return result.toString();
    }

    /**
     * Retrieves the dealer player object.
     *
     * @return The dealer player object.
     */
    public Player getDealer() {
        return dealer;
    }

    /**
     * Retrieves the human player object.
     *
     * @return The human player object.
     */
    public static Player getPlayer1() {
        return player1;
    }

    /**
     * Retrieves bot player 1 object.
     *
     * @return The bot player 1 object.
     */
    public static Player getBot1() {
        return bot1;
    }

    /**
     * Retrieves bot player 2 object.
     *
     * @return The bot player 2 object.
     */
    public static Player getBot2() {
        return bot2;
    }

    /**
     * Retrieves the hidden card of the dealer.
     *
     * @return The hidden card of the dealer.
     */
    public Card getHiddenCard() {
        return hiddenCard;
    }

    /**
     * Retrieves the image of the hidden card of the dealer based on whether the "stay" button is enabled.
     *
     * @param stayButtonEnabled Indicates if the "stay" button is enabled.
     * @return The image of the hidden card if the "stay" button is enabled; otherwise, the image path of the hidden card.
     */
    public Image getHiddenCardImage(boolean stayButtonEnabled) {
        if (stayButtonEnabled) {
            return new ImageIcon(getClass().getResource("Cards/BACK.png")).getImage();
        } else {
            return new ImageIcon(getClass().getResource(getHiddenCard().getImagePath())).getImage();
        }
    }

    /**
     * Returns the initial value of the dealer's hand.
     * If stay button is enabled, returns the value of the second card;
     * otherwise, returns the total sum of the dealer's hand.
     *
     * @param stayButtonEnabled Indicates if the stay button is enabled.
     * @return Initial value of the dealer's hand as a formatted string.
     */
    public String getDealerInitialValue(boolean stayButtonEnabled) {
        if (stayButtonEnabled) {
            return "Dealer: " + getDealer().getHand().get(1).getValue();
        } else {
            return "Dealer: " + getDealer().getSum();
        }
    }

    /**
     * Returns a list of strings representing the sums of players and the dealer.
     *
     * @return List of player and dealer sums.
     */
    public List<String> getPlayerSums() {
        List<String> playerSums = new ArrayList<>();
        playerSums.add(GameMenu.playerName + ": " + getPlayer1().getSum());
        playerSums.add("Bot1: " + getBot1().getSum());
        playerSums.add("Bot2: " + getBot2().getSum());
        playerSums.add("Dealer: " + getDealer().getSum());
        return playerSums;
    }

    /**
     * Returns the message announcing the winner.
     *
     * @return Winner message.
     */
    public String getWinnerMessage() {
        return "Results: " + determineWinner();
    }

    /**
     * Returns a list of CardImageData representing the dealer's cards.
     *
     * @param stayButtonEnabled Indicates if the stay button is enabled.
     * @param startX Starting x-coordinate for card images.
     * @param startY Starting y-coordinate for card images.
     * @return List of CardImageData for dealer's cards.
     */
    public List<CardImageData> getDealerCardsData(boolean stayButtonEnabled, int startX, int startY) {
        List<CardImageData> cardsData = new ArrayList<>();
        Player dealer = getDealer();
        for (int i = 1; i < dealer.getHand().size(); i++) {
            Card card = dealer.getHand().get(i);
            Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
            int x = startX + (120 - 80) * (i - 1);
            int y = startY;
            cardsData.add(new CardImageData(cardImage, x, y, 120, 170));
        }
        return cardsData;
    }

    /**
     * Returns a list of CardImageData representing a player's cards.
     *
     * @param player The player whose cards are being retrieved.
     * @param startX Starting x-coordinate for card images.
     * @param startY Starting y-coordinate for card images.
     * @param hideFirst If true, hides the first card image.
     * @return List of CardImageData for player's cards.
     */
    public List<CardImageData> getPlayerCardsData(Player player, int startX, int startY, boolean hideFirst) {
        List<CardImageData> cardsData = new ArrayList<>();
        for (int i = 0; i < player.getHand().size(); i++) {
            Card card = player.getHand().get(i);
            Image cardImage;
            if (hideFirst && i == 0) {
                cardImage = new ImageIcon(getClass().getResource("Cards/BACK.png")).getImage();
            } else {
                cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
            }
            int x = startX + (120 - 80) * i;
            int y = startY;
            cardsData.add(new CardImageData(cardImage, x, y, 120, 170));
        }
        return cardsData;
    }

    /**
     * Performs the hit action for the player and triggers game logic if player's sum exceeds 21.
     */
    public void hitAction() {
        playerHit();
        if (player1.getSum() > 21 || player1.getSum() == 21 ) {
            dealerTurn();
            bot1Turn();
            bot2Turn();
            setChanged();
            notifyObservers();
            System.out.println("Stay action performed");
        }
        setChanged();
        notifyObservers();
        System.out.println("Hit action performed");
    }

    /**
     * Performs the stay action for all bots and triggers game logic.
     */
    public void stayAction() {
        dealerTurn();
        bot1Turn();
        bot2Turn();
        setChanged();
        notifyObservers();
        System.out.println("Stay action performed");
    }

    /**
     * Resets the game and starts a new round.
     */
    public void retryAction() {
        new GameGUI();
        startGame(); // this method starts initialize the game
        setChanged();
        notifyObservers();
        System.out.println("Retry action performed");
    }

    /**
     * Represents data associated with a playing card image.
     */
    public static class CardImageData {
        private Image image;
        private int x;
        private int y;
        private int width;
        private int height;

        /**
         * Constructs CardImageData object.
         *
         * @param image Image of the card.
         * @param x     X-coordinate of the card image.
         * @param y     Y-coordinate of the card image.
         * @param width Width of the card image.
         * @param height Height of the card image.
         */
        public CardImageData(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        /**
         * Retrieves the image of the card.
         *
         * @return Image of the card.
         */
        public Image getImage() {
            return image;
        }

        /**
         * Retrieves the X-coordinate of the card image.
         *
         * @return X-coordinate of the card image.
         */
        public int getX() {
            return x;
        }

        /**
         * Retrieves the Y-coordinate of the card image.
         *
         * @return Y-coordinate of the card image.
         */
        public int getY() {
            return y;
        }

        /**
         * Retrieves the width of the card image.
         *
         * @return Width of the card image.
         */
        public int getWidth() {
            return width;
        }

        /**
         * Retrieves the height of the card image.
         *
         * @return Height of the card image.
         */
        public int getHeight() {
            return height;
        }
    }
    
}













    
    
