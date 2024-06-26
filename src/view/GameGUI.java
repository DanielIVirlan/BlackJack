package view;

import controller.GameLogic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * GameGR class represents the graphical user interface for a Blackjack game.
 * It extends JFrame and implements Observer to receive updates from the game controller.
 */
@SuppressWarnings({ "deprecation", "serial" })
public class GameGUI extends JFrame implements Observer {

    private static Clip cardClip;
    private GameLogic controller;

    public int cardWidth = 120;
    public int cardHeight = 170;
    

    public JPanel gamePanel, buttonPanel;
    public JButton hitButton, stayButton, retryButton, backButton;

    /**
     * Constructor for GameGRclass.
     * Initializes the GUI components and sets up the game interface.
     */
    public GameGUI() {
        initialize();
        setupButtons();
        this.controller = GameLogic.getInstance();
        controller.addObserver(this);
    }

    /**
     * Initializes the main frame properties.
     */
    public void initialize() {
        setTitle("Blackjack Game");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(new Color(35, 101, 51));
        setResizable(false);

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

        add(gamePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * Creates a styled JButton with specified text.
     * @param text The text to display on the button.
     * @return JButton instance with specified text and styling.
     */
    public JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(new Font("Typewriter", Font.ITALIC, 14));
        button.setBackground(new Color(91, 91, 91));
        button.setForeground(Color.yellow);
        button.setPreferredSize(new Dimension(200, 35));

        return button;
    }

    /**
     * Sets up action listeners for the game control buttons.
     */
    public void setupButtons() {
        hitButton = createButton("HIT");
        stayButton = createButton("STAY");
        retryButton = createButton("RETRY");
        backButton = createButton("BACK");
        backButton.addActionListener(e-> {
            playButtonClickSound("src/resources/click.wav");
            
            dispose();
            GameMenu.stopMusic();
            new GameMenu();
        });

        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        buttonPanel.add(retryButton);
        buttonPanel.add(backButton);

        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound("src/resources/click.wav");
                controller.hitAction();
                if (GameLogic.getPlayer1().getSum() > 21 || GameLogic.getPlayer1().getSum() == 21) {
                    playCardSound("src/resources/flip.wav");
                    hitButton.setEnabled(false);
                    hitButton.setForeground(Color.RED);
                    stayButton.setEnabled(false);
                    stayButton.setForeground(Color.RED);
                }
                
            }
        });

        stayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound("src/resources/click.wav");
                playCardSound("src/resources/flip.wav");
                hitButton.setEnabled(false);
                hitButton.setForeground(Color.RED);
                stayButton.setEnabled(false);
                stayButton.setForeground(Color.RED);

                controller.stayAction();
            
            }
        });

        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound("src/resources/click.wav");
                playCardSound("src/resources/flip.wav");
                dispose();
                // Handle retry action
                controller.retryAction(); // 
            
            }
        });
    }

    /**
     * Draws the current state of the game on the provided graphics context.
     * @param g The graphics context to draw on.
     */
    public void drawGame(Graphics g) {
        try {
            // Get drawing data from the controller
            Image hiddenCardImage = controller.getHiddenCardImage(stayButton.isEnabled());
            String dealerInitialValue = controller.getDealerInitialValue(stayButton.isEnabled());
            java.util.List<String> playerSums = controller.getPlayerSums();
            java.util.List<GameLogic.CardImageData> dealerCards = controller.getDealerCardsData(stayButton.isEnabled(), 100 + cardWidth - 60, 40);
            java.util.List<GameLogic.CardImageData> player1Cards = controller.getPlayerCardsData(GameLogic.getPlayer1(), 120, 500, false);
            java.util.List<GameLogic.CardImageData> bot1Cards = controller.getPlayerCardsData(GameLogic.getBot1(), 670, 40, false);
            java.util.List<GameLogic.CardImageData> bot2Cards = controller.getPlayerCardsData(GameLogic.getBot2(), 670, 500, false);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.black);

            g.drawImage(hiddenCardImage, 120, 40, cardWidth, cardHeight, null);
            g.drawString(playerSums.get(0), 120, 485);
            g.drawString(playerSums.get(1), 670, 245);
            g.drawString(playerSums.get(2), 670, 485);
            g.drawString(dealerInitialValue, 120, 245);

            drawCards(g, dealerCards);
            drawCards(g, player1Cards);
            drawCards(g, bot1Cards);
            drawCards(g, bot2Cards);

            if (!stayButton.isEnabled()) {
                String winnerMessage = controller.getWinnerMessage();
                GameLogic.gamesPlayed++;
                g.drawString(winnerMessage, 170, 350);
                g.drawString(playerSums.get(1), 670, 245);
                g.drawString(playerSums.get(2), 670, 485);
                g.drawString(playerSums.get(3), 120, 245);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws a list of card images on the provided graphics context.
     * @param g The graphics context to draw on.
     * @param cards The list of card images and their positions.
     */
    public void drawCards(Graphics g, java.util.List<GameLogic.CardImageData> cards) {
        for (GameLogic.CardImageData cardData : cards) {
            g.drawImage(cardData.getImage(), cardData.getX(), cardData.getY(), cardData.getWidth(), cardData.getHeight(), null);
        }
    }

    /**
     * Plays a card flip sound effect.
     * @param audioFilePath The file path to the sound effect.
     */
    public static void playCardSound(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            cardClip = AudioSystem.getClip();
            cardClip.open(audioInputStream);
            cardClip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays a button click sound effect.
     * @param audioFilePath The file path to the sound effect.
     */
    public void playButtonClickSound(String audioFilePath) {
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

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Update received from Controller");
        repaint();
    }
}
