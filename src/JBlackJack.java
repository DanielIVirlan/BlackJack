import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class JBlackJack extends GameGUI {

    public static String playerName = "";
    public static String avatarFileName = "";
    private static Clip backgroundClip;
    private static Clip cardClip;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame();
        frame.setTitle("Menu iniziale");
        frame.setSize(1000, 800);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ImageIcon img = new ImageIcon("src\\resources\\sfondoMenu.png");
        img = resizeImageIcon(img, 1000, 800);

        JLabel panel = new JLabel(img);
        panel.setLayout(new GridBagLayout());
        frame.setContentPane(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 0, 5, 0);

        JButton startButton = new JButton("START GAME");
        startButton.addActionListener(e -> {
            

            GameLogic.gamesPlayed = 0;
            GameLogic.gamesWon = 0;
            GameLogic.gamesLost = 0;

            playerName = JOptionPane.showInputDialog("Inserisci il tuo nome");
            if (playerName == null || playerName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Errore: Il nome deve essere inserito!");
                return;
            }

            String gender = JOptionPane.showInputDialog("Male or female? (m/f)");
            if (gender == null || (!gender.trim().equalsIgnoreCase("m") && !gender.trim().equalsIgnoreCase("f"))) {
                JOptionPane.showMessageDialog(frame, "Errore: Inserire solo 'm' o 'f'!");
                
                return;
            }

            
            avatarFileName = gender.trim().equalsIgnoreCase("m") ? "src\\avatar\\Male.png" : "src\\avatar\\Female.png";
            
            playCardSound("src\\resources\\cardsound.wav");
            new GameGUI();
            frame.dispose();
        });

        JButton statisticsButton = new JButton("STATISTICS");
        statisticsButton.addActionListener(e -> {
            Stats.showStats(playerName, avatarFileName);
            frame.dispose();
        });

        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(e -> frame.dispose());

        startButton.setFocusable(false);
        statisticsButton.setFocusable(false);
        exitButton.setFocusable(false);

        Dimension buttonSize = new Dimension(200, 35);
        startButton.setPreferredSize(buttonSize);
        statisticsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        startButton.addActionListener(e -> playButtonClickSound("src\\resources\\click.wav"));
        statisticsButton.addActionListener(e -> playButtonClickSound("src\\resources\\click.wav"));
        exitButton.addActionListener(e -> playButtonClickSound("src\\resources\\click.wav"));

        startButton.setBackground(new Color(91, 91, 91));
        statisticsButton.setBackground(new Color(91, 91, 91));
        exitButton.setBackground(new Color(91, 91, 91));

        Font buttonFont = new Font("Typewriter", Font.ITALIC, 14);
        startButton.setFont(buttonFont);
        statisticsButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        startButton.setForeground(Color.green);
        statisticsButton.setForeground(Color.yellow);
        exitButton.setForeground(Color.red);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(startButton, gbc);
        gbc.gridy++;
        panel.add(statisticsButton, gbc);

        // Add music toggle button before exit button
        JToggleButton musicToggle = new JToggleButton("MUSIC ON");
        musicToggle.setPreferredSize(buttonSize);
        musicToggle.setBackground(new Color(91, 91, 91));
        musicToggle.setFont(buttonFont);
        musicToggle.setForeground(Color.blue);
        musicToggle.setFocusable(false);
        musicToggle.addActionListener(e -> {
            playButtonClickSound("src\\resources\\click.wav");
            if (musicToggle.isSelected()) {
                musicToggle.setText("MUSIC OFF");
                stopBackgroundMusic();
            } else {
                musicToggle.setText("MUSIC ON");
                playBackgroundMusic("src\\resources\\music.wav");
            }
        });

        gbc.gridy++;
        panel.add(musicToggle, gbc);

        gbc.gridy++;
        panel.add(exitButton, gbc);

        panel.setBackground(new Color(35, 101, 51));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Play music when the menu is opened
        playBackgroundMusic("src\\resources\\music.wav");
    }

    private static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

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

    private static void playBackgroundMusic(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioInputStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
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

    private static void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }
}
