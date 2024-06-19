import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The Stats class is responsible for displaying game-related statistics for a player,
 * including the player's name, avatar, and the number of games played, won, and lost.
 */
public class Stats {

    /**
     * Displays the statistics window for the player. This includes the player's name,
     * avatar, and game statistics such as games played, games won, and games lost.
     *
     * @param playerName     The name of the player.
     * @param avatarFilePath The file path of the player's avatar image.
     */
    public static void showStats(String playerName, String avatarFilePath) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Stats");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLayout(new BorderLayout());
            frame.setBackground(new Color(35, 101, 51));
            frame.setResizable(false);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBackground(new Color(35, 101, 51));

            JPanel countPanel = new JPanel();
            countPanel.setOpaque(false);

            JLabel avatarLabel = new JLabel();
            if (avatarFilePath != null) {
                ImageIcon avatarIcon = new ImageIcon(avatarFilePath);
                avatarLabel.setIcon(avatarIcon);
            }
            countPanel.add(avatarLabel);

            JLabel countLabel = new JLabel("<html>"
                    + "<p>" + playerName + " has played: " + GameLogic.gamesPlayed + " games</p>"
                    + "<p style='margin-top: 20px;'>Games won: " + GameLogic.gamesWon
                    + "<p>Games lost: " + GameLogic.gamesLost
                    + "</html>");

            countLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            countLabel.setForeground(Color.WHITE);
            countPanel.add(countLabel);

            mainPanel.add(countPanel, BorderLayout.CENTER);

            JPanel southPanel = new JPanel();
            southPanel.setOpaque(false);

            JButton backButton = new JButton("Back");
            backButton.setForeground(Color.RED);
            backButton.setFocusable(false);

            // Set action listener to play a click sound and return to main menu
            backButton.addActionListener(e -> playButtonClickSound("src\\resources\\click.wav"));

            // Customize button font and color
            Font buttonFont = new Font("Typewriter", Font.ITALIC, 14);
            backButton.setFont(buttonFont);
            backButton.setForeground(Color.red);

            // Action to close the current frame and open the main menu frame
            backButton.addActionListener(e -> {
                frame.dispose(); // Close the statistics frame
                JBlackJack.main(new String[0]); // Launch the main menu
            });

            southPanel.add(backButton);
            mainPanel.add(southPanel, BorderLayout.SOUTH);
            frame.add(mainPanel);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }

    /**
     * Plays a sound from the specified audio file path. This is typically used for button click sounds.
     *
     * @param audioFilePath The file path of the audio file to be played.
     */
    private static void playButtonClickSound(String audioFilePath) {
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
}






