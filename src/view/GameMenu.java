/**
 * This class represents the view of a Blackjack game GUI.
 * It includes menus, statistics panels, game setup, and sound functionalities.
 */
package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import controller.GameLogic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * The BlackjackView class extends JFrame and implements the GUI for the game.
 */
@SuppressWarnings("serial")
public class GameMenu extends JFrame {
	
	private static Clip cardClip;
	private static Clip backgroundClip;

	public static String avatarFileName;
	public static String playerName;
	
	public int cardWidth = 120, cardHeight = 170;
	
	private JPanel mainPanel;
    private JLabel menuPanel;
    private JPanel statsPanel;
    public JPanel gamePanel;
    private JButton startButton;
    private JButton statisticsButton;
    private JButton exitButton;
    private JButton backStatsButton;
    private JToggleButton musicToggle;
    private static JLabel avatarLabel;
    private JLabel countLabel;
    private JLabel levelLabel;
    private JProgressBar progressBar;
    
	private static Frame BlackJackView;

    /**
     * Constructor for the BlackjackView class.
     * Sets up the initial UI and initializes components.
     */
    public GameMenu() {
    	playSound("src/resources/music.wav");
    	
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        initializeUI();
    }

    /**
     * Initializes the main UI components.
     */
    public void initializeUI() {
        setTitle("BlackJack");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setResizable(false);

        mainPanel = new JPanel(new CardLayout());
        add(mainPanel);

        setupMenuPanel();
        setupStatsPanel();
        
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(statsPanel, "Stats");
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Sets up the menu panel with buttons and layout.
     */
    public void setupMenuPanel() {
        ImageIcon img = new ImageIcon("src/resources/sfondoMenu.png");
        img = resizeImageIcon(img, 1000, 800);

        menuPanel = new JLabel(img);
        menuPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 0, 5, 0);

        startButton = createButton("START GAME", Color.GREEN);
        statisticsButton = createButton("STATISTICS", Color.YELLOW);
        exitButton = createButton("EXIT", Color.RED);
        musicToggle = createToggleButton("MUSIC OFF", Color.BLUE);

        addToPanel(menuPanel, startButton, gbc);
        gbc.gridy++;
        addToPanel(menuPanel, statisticsButton, gbc);
        gbc.gridy++;
        addToPanel(menuPanel, musicToggle, gbc);
        gbc.gridy++;
        addToPanel(menuPanel, exitButton, gbc);
    }
    
    /**
     * Adds a component to a panel using the provided constraints.
     * @param panel The panel to which the component will be added.
     * @param component The component to be added to the panel.
     * @param gbc The GridBagConstraints specifying how the component should be added.
     */
    public void addToPanel(JLabel panel, Component component, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }

    /**
     * Sets up the statistics panel with player stats and controls.
     */
    public void setupStatsPanel() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new BorderLayout());
        statsPanel.setBackground(new Color(35, 101, 51));

        JPanel countPanel = new JPanel();
        countPanel.setOpaque(false);
        countLabel = new JLabel(); 
        countLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        countLabel.setForeground(Color.WHITE);
        countPanel.add(countLabel);
        
        avatarLabel = new JLabel();
        countPanel.add(avatarLabel);
        
        int level = GameLogic.gamesWon / 10;
        int progress = (GameLogic.gamesWon % 10) * 10;

        levelLabel = new JLabel("Livello: " + level);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 20));
        levelLabel.setForeground(Color.WHITE);
        countPanel.add(levelLabel);

        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(progress);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.RED);
        progressBar.setPreferredSize(new Dimension(200, 30));
        countPanel.add(progressBar);

        statsPanel.add(countPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);

        backStatsButton = new JButton("BACK");
        backStatsButton.setForeground(Color.RED);
        backStatsButton.setFocusable(false);
        backStatsButton.setFont(new Font("Typewriter", Font.ITALIC, 14));
        southPanel.add(backStatsButton);
        statsPanel.add(southPanel, BorderLayout.SOUTH);
        
        backStatsButton.addActionListener(e -> {
            playButtonClickSound("src/resources/click.wav");
            showMenuPanel();
        });
        
        exitButton.addActionListener(e -> {
            playButtonClickSound("src/resources/click.wav");
            System.exit(0);
        });
        
        startButton.addActionListener(e -> {
            GameLogic.gamesPlayed = 0;
            GameLogic.gamesWon = 0;
            GameLogic.gamesLost = 0;
            playButtonClickSound("src/resources/click.wav");

            playerName = promptPlayerName();
            if (playerName == null || playerName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(BlackJackView ,"Error: A name must be entered!");
                return;
            }
            selectAvatar();
            dispose();

            new GameGUI();
        });
        
        statisticsButton.addActionListener(e -> {
            playButtonClickSound("src/resources/click.wav");
            showStatsPanel();
        });
        
        musicToggle.addActionListener(e -> {
            JToggleButton musicToggle = (JToggleButton) e.getSource();
            playButtonClickSound("src/resources/click.wav");
            
            if (musicToggle.isSelected()) {
                stopMusic();
            } else {
                playBackgroundMusic("src/resources/music.wav");
            }
        });
    }
    
    /**
     * Creates a JButton with specified text and color.
     * @param text The text on the button
     * @param color The background color of the button
     * @return The created JButton
     */
    public JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(200, 35));
        button.setBackground(new Color(91, 91, 91));
        button.setFont(new Font("Typewriter", Font.ITALIC, 14));
        button.setForeground(color);
        return button;
    }

    /**
     * Creates a JToggleButton with specified text and color.
     * @param text The text on the toggle button
     * @param color The background color of the toggle button
     * @return The created JToggleButton
     */
    public JToggleButton createToggleButton(String text, Color color) {
        JToggleButton toggleButton = new JToggleButton(text);
        toggleButton.setFocusable(false);
        toggleButton.setPreferredSize(new Dimension(200, 35));
        toggleButton.setBackground(new Color(91, 91, 91));
        toggleButton.setFont(new Font("Typewriter", Font.ITALIC, 14));
        toggleButton.setForeground(color);
        return toggleButton;
    }

    /**
     * Resizes an ImageIcon to the specified width and height.
     * @param icon The ImageIcon to resize
     * @param width The target width
     * @param height The target height
     * @return The resized ImageIcon
     */
    public static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    /**
     * Shows the menu panel.
     */
    public void showMenuPanel() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "Menu");
    }

    /**
     * Shows the statistics panel and updates the player stats.
     */
    public void showStatsPanel() {
    	updateAvatarLabel();
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "Stats");
        countLabel.setText("<html><p>" + playerName + " has played: " + GameLogic.gamesPlayed + " games</p>"
                + "<p style='margin-top: 20px;'>Games won: " + GameLogic.gamesWon + "<p>Games lost: " + GameLogic.gamesLost + "</html>");
        int level = GameLogic.gamesWon / 10;
        int progress = (GameLogic.gamesWon % 10) * 10;
        progressBar.setValue(progress);
        levelLabel.setText("Livello: " + level);
    } 
    
    
    public static void selectAvatar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create and add Male avatar button
        ImageIcon maleIcon = new ImageIcon("src/resources/Male.png");
        maleIcon = resizeImageIcon(maleIcon, 100, 120);
        JButton maleButton = new JButton("Male", maleIcon);
        maleButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        maleButton.setHorizontalTextPosition(SwingConstants.CENTER);
        maleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                avatarFileName = "src/resources/Male.png";
                updateAvatarLabel();
                ((JButton) e.getSource()).getRootPane().getParent().setVisible(false); 
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(maleButton, gbc);

        // Create and add Female avatar button
        ImageIcon femaleIcon = new ImageIcon("src/resources/Female.png");
        femaleIcon = resizeImageIcon(femaleIcon, 100, 120);
        JButton femaleButton = new JButton("Female", femaleIcon);
        femaleButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        femaleButton.setHorizontalTextPosition(SwingConstants.CENTER);
        femaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                avatarFileName = "src/resources/Female.png";
                updateAvatarLabel();
                ((JButton) e.getSource()).getRootPane().getParent().setVisible(false); // Close the option dialog
            }
        });
        gbc.gridx = 1;
        panel.add(femaleButton, gbc);

        // Display the dialog for avatar selection
        JDialog dialog = new JDialog(BlackJackView, "Select Avatar", true);
        dialog.setContentPane(panel);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(BlackJackView);
        dialog.setVisible(true);
    }

    /**
     * Updates the avatar label with the selected avatar image.
     */
    public static void updateAvatarLabel() {
        if (avatarFileName != null) {
            ImageIcon avatarIcon = new ImageIcon(avatarFileName);
            avatarLabel.setIcon(avatarIcon);
        }
    }

    
    /**
     * Plays a sound from a given audio file path.
     * @param audioFilePath The path to the audio file.
     */
    public static void playSound(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            if (audioFilePath.equals("src/resources/music.wav")) {
                backgroundClip = clip;
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Plays a button click sound.
     * @param audioFilePath The path to the audio file for button click sound.
     */
    public static void playButtonClickSound(String audioFilePath) {
        playSound(audioFilePath);
    }

    /**
     * Plays background music.
     * @param audioFilePath The path to the audio file for background music.
     */
    public void playBackgroundMusic(String audioFilePath) {
        if (backgroundClip == null || !backgroundClip.isRunning()) {
            playSound(audioFilePath);
        }
    }

    /**
     * Stops the currently playing background music.
     */
    public static void stopMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null; // Reset the clip
        }
    }

    /**
     * Prompts the player to enter their name via a dialog.
     * @return The entered player name.
     */
    public String promptPlayerName() {
        return JOptionPane.showInputDialog("Enter your name:");
    }

    /**
     * Plays a card sound.
     * @param audioFilePath The path to the audio file for card sound.
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
}
