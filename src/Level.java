import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Level {
    private JFrame frame;
    private JProgressBar progressBar;
    private Timer timer;
    private int progress;

    public Level() {
        frame = new JFrame("Progress Bar Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(null); // Usiamo un layout null per posizionare i componenti manualmente
        frame.getContentPane().setBackground(new Color(31, 105, 51)); // Impostiamo il colore di sfondo del content pane del frame

        // Creiamo una JProgressBar e la posizioniamo
        progressBar = new JProgressBar();
        progressBar.setBounds(1000, 50, 200, 30); // Impostiamo le dimensioni e la posizione della barra di avanzamento
        progressBar.setMinimum(0); // Valore minimo della barra di avanzamento
        progressBar.setMaximum(100); // Valore massimo della barra di avanzamento
        frame.add(progressBar); // Aggiungiamo la barra di avanzamento al frame

        // Creiamo un timer per incrementare la barra di avanzamento
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (progress <= 100) {
                    progressBar.setValue(progress); // Impostiamo il valore della barra di avanzamento
                    progress++; // Incrementiamo il valore per il prossimo aggiornamento
                } else {
                    timer.stop(); // Ferma il timer quando raggiungiamo il 100%
                    JOptionPane.showMessageDialog(frame, "Caricamento completato!");
                }
            }
        });

        // Avviamo il timer per iniziare l'incremento della barra di avanzamento
        timer.start();

        frame.setVisible(true); // Rendiamo visibile il frame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Level(); // Creiamo un'istanza della classe per mostrare la finestra
            }
        });
    }
}


