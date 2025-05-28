package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse QuizSelection bietet eine grafische Benutzeroberfläche zur Auswahl von Quizzes.
 * Sie zeigt eine Liste von verfügbaren Quizzes an, die der Benutzer starten kann, und bietet Optionen zum Erstellen neuer Quizzes.
 * Die Benutzeroberfläche enthält auch Menüoptionen zum Ändern des Themas, Zurücksetzen des Passworts und Abmelden.
 */
public class QuizSelection extends JFrame {
    private List<JButton> buttons = new ArrayList<>();

    /**
     * Konstruktor für die Erstellung der grafischen Benutzeroberfläche zur Quizauswahl.
     * Initialisiert die Benutzeroberfläche und fügt die verschiedenen Komponenten hinzu.
     */
    public QuizSelection() {
        super("Quiz Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        int fixedWidth = 500;
        int maxWidth = 1000;
        int maxHeight = 800;

        // Menüleiste erstellen
        JMenuBar menuBar = new JMenuBar();
        JMenu einstellungenMenu = new JMenu("Einstellungen");

        // Menüpunkt zum Wechseln des Themas
        JMenuItem themeItem = new JMenuItem("Theme wechseln");
        themeItem.addActionListener(e -> ThemeSwitcher.showThemeDialog(this));
        einstellungenMenu.add(themeItem);

        String User = Session.getUsername();

        // Menüpunkt zum Zurücksetzen des Passworts
        JMenuItem resetPasswordItem = new JMenuItem("Passwort zurücksetzen");
        resetPasswordItem.addActionListener(e -> {
            // Check if the user is logged in
            if (Session.isLoggedIn()) {
                // Open password reset dialog for logged-in user
                new NeuesPasswort(User);
                dispose(); // Close the current window
            } else {
                // Open password reset dialog with security question
                new NeuesPasswort();
                dispose(); // Close the current window
            }
        });
        einstellungenMenu.add(resetPasswordItem);

        // Neuer Menüpunkt für Logout
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            Session.logout(); // Benutzer ausloggen
            new LoginGUI(); // Zurück zur Login-GUI
            dispose(); // Aktuelles Fenster schließen
        });
        einstellungenMenu.add(logoutItem);

        menuBar.add(einstellungenMenu);
        setJMenuBar(menuBar);

        // Hauptpanel mit Quizbuttons
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Quiz Selection");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        // Benutzername aus der Session
        String currentUser = Session.getUsername();

        File quizDir = new File("C:/temp/Quiz");
        File[] files = quizDir.listFiles((dir, name) ->
                name.endsWith("." + currentUser + ".txt") || name.endsWith(".Admin.txt"));

        int row = 1;

        // Quiz-Dateien auflisten und Buttons erstellen
        if (files != null && files.length > 0) {
            for (File file : files) {
                String fileName = file.getName();
                String suffixUser = "." + currentUser + ".txt";
                String suffixAdmin = ".Admin.txt";

                String buttonLabel;
                if (fileName.endsWith(suffixUser)) {
                    buttonLabel = fileName.substring(0, fileName.length() - suffixUser.length());
                } else if (fileName.endsWith(suffixAdmin)) {
                    buttonLabel = fileName.substring(0, fileName.length() - suffixAdmin.length());
                } else {
                    buttonLabel = fileName; // Fallback, sollte eigentlich nicht passieren
                }

                // Button für jedes Quiz erstellen
                JButton quizButton = new JButton(buttonLabel);
                quizButton.setToolTipText("Start Quiz: " + buttonLabel);
                quizButton.addActionListener((ActionEvent e) -> {
                    new QuizGUI(file.getName()); // z.B. "mathe.benutzer.txt"
                });
                gbc.gridy = row++;
                panel.add(quizButton, gbc);
                buttons.add(quizButton);
            }
        } else {
            // Meldung anzeigen, wenn keine Quiz-Dateien gefunden wurden
            JLabel errorLabel = new JLabel("Keine Quiz-Dateien für Benutzer \"" + currentUser + "\" gefunden.");
            gbc.gridx = 0;
            gbc.gridy = row++;
            panel.add(errorLabel, gbc);
        }

        // Button zum Erstellen eines neuen Quizzes
        JButton erstellenButton = new JButton("Neues Quiz erstellen");
        erstellenButton.setToolTipText("Öffnet das Fenster zum Erstellen eines neuen Quiz");
        erstellenButton.addActionListener(e -> {
            new QuizErstellenGUI();
        });
        gbc.gridy = row++;
        panel.add(erstellenButton, gbc);
        buttons.add(erstellenButton);

        add(panel);
        panel.setPreferredSize(new Dimension(fixedWidth, panel.getPreferredSize().height));
        pack();
        setLocationRelativeTo(null);
        setMaximumSize(new Dimension(maxWidth, maxHeight));

        // Component Listener zum Anpassen der Schriftgröße bei Fenstergrößenänderung
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                aktualisiereFonts(label, buttons);
            }
        });

        setVisible(true);
    }

    /**
     * Aktualisiert die Schriftgrößen der Label und Buttons basierend auf der Fensterbreite.
     * Dies stellt sicher, dass die Schriftgrößen proportional zur Fenstergröße sind.
     *
     * @param label Das Label, dessen Schriftgröße aktualisiert werden soll.
     * @param buttons Die Liste der Buttons, deren Schriftgrößen aktualisiert werden sollen.
     */
    private void aktualisiereFonts(JLabel label, List<JButton> buttons) {
        int breite = getWidth();
        float faktor = breite / 500.0f;

        int labelFontSize = Math.round(20 * faktor);
        int buttonFontSize = Math.round(16 * faktor);

        label.setFont(new Font("Arial", Font.BOLD, labelFontSize));
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.PLAIN, buttonFontSize));
        }
    }
}
