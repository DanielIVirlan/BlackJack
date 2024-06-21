import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;

/**
 * The GameGUI class manages the graphical user interface for a Blackjack-like card game.
 * It includes buttons for player actions (hit, stay, retry, exit), displays cards for
 * the dealer and players, handles game logic, and plays sound effects on button clicks.
 */
public class GameGUI {
    private GameLogic gameLogic;    // Game logic instance
    private JFrame frame;           // Main JFrame for the game GUI
    private JPanel gamePanel;       // Panel for displaying the game graphics
    private JPanel buttonPanel;     // Panel for buttons (hit, stay, retry, exit)
    private JButton hitButton;      // Button for player to hit
    private JButton stayButton;     // Button for player to stay
    private JButton retryButton;    // Button to retry the game
    private JButton exitButton;     // Button to exit the game
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  // Screen size
    private int cardWidth = 170;    // Width of a card image
    private int cardHeight = 250;   // Height of a card image
    private static Clip cardClip;

    /**
     * Constructs a GameGUI object, initializes the game logic, and sets up the GUI components.
     */
    public GameGUI() {
        gameLogic = new GameLogic();
        setupGUI();
    }

    /**
     * Sets up the main JFrame, game panel, button panel, and initializes GUI components.
     */
    private void setupGUI() {
        frame = new JFrame("BlackJack");
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(35, 101, 51));

        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(35, 101, 51));
        setupButtons();

        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /**
     * Sets up the buttons (hit, stay, retry, exit) with their respective actions and styles.
     */
    private void setupButtons() {
        hitButton = createButton("HIT", e -> onHit());
        stayButton = createButton("STAY", e -> onStay());
        retryButton = createButton("RETRY", e -> onRetry());
        exitButton = createButton("BACK", e -> onExit());

        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        buttonPanel.add(retryButton);
        buttonPanel.add(exitButton);
    }

    /**
     * Creates a JButton with the specified text, action listener, and visual style.
     *
     * @param text   The text displayed on the button.
     * @param action The ActionListener for handling button clicks.
     * @return The configured JButton.
     */
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(new Font("Typewriter", Font.ITALIC, 14));
        button.setBackground(new Color(91, 91, 91));
        button.setForeground(Color.yellow);
        button.setPreferredSize(new Dimension(200, 35));
        button.addActionListener(action);
        button.addActionListener(e -> playButtonClickSound("src\\resources\\click.wav"));
        return button;
    }

    /**
     * Handles the action when the HIT button is clicked by the player.
     * Draws a new card for the player and updates the game panel.
     */
    private void onHit() {
        playCardSound("src\\resources\\flip.wav");
        gameLogic.playerHit();
        if (gameLogic.getPlayer1().getSum() > 21) {
            playCardSound("src\\resources\\flip.wav");
            hitButton.setEnabled(false);
            hitButton.setForeground(Color.RED);
            onStay();
        }
        gamePanel.repaint();
    }

    /**
     * Handles the action when the STAY button is clicked by the player.
     * Disables player actions, initiates turns for the dealer and bots, and updates the game panel.
     */
    private void onStay() {
        playCardSound("src\\resources\\flip.wav");
        GameLogic.gamesPlayed++;
        hitButton.setEnabled(false);
        hitButton.setForeground(Color.RED);
        stayButton.setEnabled(false);
        stayButton.setForeground(Color.RED);
        

        gameLogic.dealerTurn();
        gameLogic.bot1Turn();
        gameLogic.bot2Turn();
        gamePanel.repaint();
    }

    /**
     * Handles the action when the RETRY button is clicked to start a new game.
     * Disposes the current frame and creates a new instance of GameGUI.
     */
    private void onRetry() {
        playCardSound("src\\resources\\cardsound.wav");
        frame.dispose();
        new GameGUI();
        
    }

    /**
     * Handles the action when the EXIT button is clicked to return to the main menu.
     * Disposes the current frame and launches the main menu of the game.
     */

    
    
    private void onExit() {
        frame.dispose();
        JBlackJack.main(new String[0]);
    }

    /**
     * Draws the game graphics including cards for the dealer and players,
     * and displays the winner when the game is over.
     *
     * @param g The Graphics context to draw on.
     */
    private void drawGame(Graphics g) {
        try {
 
            String dealerInitialValue;
            // Draw the dealer's hidden card
            Image hiddenCardImage;
            if (stayButton.isEnabled()) {
                hiddenCardImage = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                dealerInitialValue = "Dealer: " + gameLogic.getDealer().getHand().get(1).getValue();
            } else {
                hiddenCardImage = new ImageIcon(getClass().getResource(gameLogic.getHiddenCard().getImagePath())).getImage();
                dealerInitialValue = "Dealer: " + gameLogic.getDealer().getSum();
            }
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.setColor(Color.black);
            g.drawImage(hiddenCardImage, 250, 20, cardWidth, cardHeight, null);
            g.drawString(JBlackJack.playerName +":" + gameLogic.getPlayer1().getSum(), 250, 635);
                g.drawString("Bot1: " + gameLogic.getBot1().getSum(), 1350, 300);
                g.drawString("Bot2: " + gameLogic.getBot2().getSum(), 1350, 635);
                g.drawString(dealerInitialValue , 250, 300);

                if(gameLogic.getPlayer1().getSum() == 21){
                    g.drawString(JBlackJack.playerName +":" + gameLogic.getPlayer1().getSum()+ " Has BlackJack", 250, 635);
                    onStay();
                }

                if(gameLogic.getBot1().getSum() == 21){
                    g.drawString("Bot1: " + gameLogic.getBot1().getSum()+ " Has BlackJack", 1350, 300);                    
                }

                if(gameLogic.getBot2().getSum() == 21){
                    g.drawString("Bot2: " + gameLogic.getBot2().getSum()+ " Has BlackJack", 1350, 635);                    
                }

                if(gameLogic.getDealer().getSum() == 21){
                    g.drawString("Dealer: " + gameLogic.getDealer().getSum()+ " Has BlackJack" , 250, 300);                    
                }
            // Draw the rest of the dealer's cards
            drawDealerCards(g, gameLogic.getDealer(), 250 + cardWidth + -60, 20, stayButton.isEnabled());

            // Draw cards for other players
            drawPlayerCards(g, gameLogic.getPlayer1(), 250, 650, false);
            drawPlayerCards(g, gameLogic.getBot1(), 1350, 20, false);
            drawPlayerCards(g, gameLogic.getBot2(), 1350, 650, false);

            // Display the winner if the game is over
            if (!stayButton.isEnabled()) {
                String winner = gameLogic.determineWinner();
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.black);
                g.drawString(winner , 600, 450);
                // aggiungo i punteggi delle mani dei giocatori
                g.drawString(JBlackJack.playerName +":" + gameLogic.getPlayer1().getSum(), 250, 635);
                g.drawString("Bot1: " + gameLogic.getBot1().getSum(), 1350, 300);
                g.drawString("Bot2: " + gameLogic.getBot2().getSum(), 1350, 635);
                g.drawString("Dealer: " + gameLogic.getDealer().getSum(), 250, 300);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the cards for the dealer based on their hand.
     *
     * @param g         The Graphics context to draw on.
     * @param dealer    The dealer Player object.
     * @param startX    The starting X coordinate to draw cards.
     * @param startY    The starting Y coordinate to draw cards.
     * @param hideFirst Whether to hide the first card (for initial hidden card).
     */
    private void drawDealerCards(Graphics g, Player dealer, int startX, int startY, boolean hideFirst) {
        for (int i = 1; i < dealer.getHand().size(); i++) { // Start from 1 to skip the hidden card
            Card card = dealer.getHand().get(i);
            Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
            g.drawImage(cardImage, startX + (cardWidth - 60) * (i - 1), startY, cardWidth, cardHeight, null);
        }
    }

    /**
     * Draws the cards for a player based on their hand.
     *
     * @param g         The Graphics context to draw on.
     * @param player    The Player object representing the player.
     * @param startX    The starting X coordinate to draw cards.
     * @param startY    The starting Y coordinate to draw cards.
     * @param hideFirst Whether to hide the first card (for initial hidden card).
     */
    private void drawPlayerCards(Graphics g, Player player, int startX, int startY, boolean hideFirst) {
        for (int i = 0; i < player.getHand().size(); i++) {
            Card card = player.getHand().get(i);
            Image cardImage;
            if (hideFirst && i == 0) {
                
                cardImage = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
            } else {
                cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
            }
            g.drawImage(cardImage, startX + (cardWidth - 60) * i, startY, cardWidth, cardHeight, null);
        }
    }

    /**
     * Plays a button click sound from the specified audio file path.
     *
     * @param audioFilePath The file path of the audio file to be played.
     */
    private void playButtonClickSound(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void playCardSound(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            cardClip = AudioSystem.getClip();
            cardClip.open(audioInputStream);
            cardClip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Main method to start the game GUI. Increments the gamesPlayed counter and sets the Nimbus look and feel.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        
        System.out.println("Games played: " + GameLogic.gamesPlayed);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new GameGUI();
    }
}





