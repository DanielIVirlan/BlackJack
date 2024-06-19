import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip; 
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The main class that initializes and runs the Blackjack game menu.
 */
public class JBlackJack extends GameGUI {

    /** The name of the player. */
    public static String playerName = "";

    /** The file path of the player's avatar image. */
    public static String avatarFileName = "";

    /**
     * The main method that starts the Blackjack game menu.
     *
     * @param args The command line arguments (not used).
     * 
     */

    

    public static void main(String[] args){

       

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Initialize and configure the main menu frame
        JFrame frame = new JFrame();
        frame.setTitle("Menu iniziale");
        frame.setSize(1000,800);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set background image for the main menu
        ImageIcon img = new ImageIcon("src\\resources\\sfondoMenu.png");
        img = resizeImageIcon(img,1000,800);

        JLabel panel = new JLabel(img);
        panel.setLayout(new GridBagLayout());
        frame.setContentPane(panel);

        // Constraints for GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 0, 5, 0);

        

        // START GAME button setup
        JButton startbutton = new JButton("START GAME");
        startbutton.addActionListener(e -> {
            GameLogic.gamesPlayed = 0;
            GameLogic.gamesWon = 0;
            GameLogic.gamesLost = 0;
            
            playerName = JOptionPane.showInputDialog("Inserisci il tuo nome");
            if (playerName == null) {
                // User pressed Cancel, do nothing
                return;
            } else if (playerName.trim().isEmpty()) {
                // User left the field empty and pressed OK, show error message
                JOptionPane.showMessageDialog(frame, "Errore: Il nome deve essere inserito!");
                
                return;
            }

            String gender = JOptionPane.showInputDialog("Male or female? (m/f)");
            if (gender == null) {
                // User pressed Cancel, do nothing
                return;
            } else if (!gender.trim().equalsIgnoreCase("m") && !gender.trim().equalsIgnoreCase("f")) {
                // User entered a value other than "m" or "f", show error message
                JOptionPane.showMessageDialog(frame, "Errore: Inserire solo 'm' o 'f'!");
                return;
            }

            avatarFileName = gender.trim().equalsIgnoreCase("m") ? "src\\avatar\\Male.png" : "src\\avatar\\Female.png";

            // Start the game with player data
            GameGUI.main(new String[]{playerName, avatarFileName});

            // Close the main menu
            frame.dispose();

        });

        // STATISTICS button setup
        JButton statistics = new JButton("STATISTICS");
        statistics.addActionListener(e -> {
            // Open statistics window with player data
            Stats.showStats(playerName, avatarFileName);

            // Close the main menu
            frame.dispose();
        });

        // EXIT button setup
        JButton exit = new JButton("EXIT");
        exit.addActionListener(e -> {
            frame.dispose(); // Close the menu frame
        });

        // Set focusable, preferred size, and action listener for buttons
        startbutton.setFocusable(false);
        statistics.setFocusable(false);
        exit.setFocusable(false);

        Dimension buttonSize = new Dimension(200,35);
        startbutton.setPreferredSize(buttonSize);
        statistics.setPreferredSize(buttonSize);
        exit.setPreferredSize(buttonSize);

        // Add sound effect on button click
        startbutton.addActionListener(e -> playButtonClickSound("src\\resources\\click.wav"));
        statistics.addActionListener(e -> playButtonClickSound("src\\resources\\click.wav"));
        exit.addActionListener(e -> playButtonClickSound("src\\resources\\click.wav"));

        // Set button colors and font
        startbutton.setBackground(new Color(91, 91, 91));
        statistics.setBackground(new Color(91, 91, 91));
        exit.setBackground(new Color(91, 91, 91));

        Font buttonFont = new Font("Typewriter", Font.ITALIC, 14);
        startbutton.setFont(buttonFont);
        statistics.setFont(buttonFont);
        exit.setFont(buttonFont);

        // Set text colors for buttons
        startbutton.setForeground(Color.green);
        statistics.setForeground(Color.yellow);
        exit.setForeground(Color.red);

        // Add buttons to the panel with GridBagConstraints
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(startbutton,gbc);
        gbc.gridy++;
        panel.add(statistics,gbc);
        gbc.gridy++;
        panel.add(exit,gbc);

        // Set background color for the panel
        panel.setBackground(new Color(35,101,51));

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Resizes an ImageIcon to the specified width and height.
     *
     * @param icon  The ImageIcon to resize.
     * @param width The desired width of the resized image.
     * @param height The desired height of the resized image.
     * @return The resized ImageIcon.
     */
    private static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height){
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(width,height,Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    /**
     * Plays a button click sound from the specified audio file path.
     *
     * @param audioFilePath The file path of the audio file to play.
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


