package src;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Haupt-GUI-Fenster zur Auswahl und Verwaltung von Quizzen.
 * Bietet Funktionalität zum Starten von Quizzen, Erstellen neuer Quizze, Bearbeiten bestehender Quizze
 * und Verwalten von Benutzereinstellungen wie Themen und Passwörtern.
 */
public class QuizSelection extends JFrame {
    private List<JButton> buttons = new ArrayList<>();
    private JPanel panel;
    private JLabel label;

    /**
     * Erstellt das QuizSelection-Fenster mit Menüleiste und Quizliste.
     * Richtet die Benutzeroberfläche für die Quizverwaltung und -auswahl ein.
     */
    public QuizSelection() {
        super("Quiz Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        int fixedWidth = 500;
        int maxWidth = 1000;
        int maxHeight = 800;

        /**
         * Menüleiste einrichten
         */
        JMenuBar menuBar = new JMenuBar();
        JMenu einstellungenMenu = new JMenu("Einstellungen");

        JMenuItem themeItem = new JMenuItem("Theme wechseln");
        themeItem.addActionListener(e -> ThemeSwitcher.showThemeDialog(this));
        einstellungenMenu.add(themeItem);

        String user = Session.getUsername();

        JMenuItem resetPasswordItem = new JMenuItem("Passwort zurücksetzen");
        resetPasswordItem.addActionListener(e -> {
            if (Session.isLoggedIn()) {
                new NeuesPasswort(user);
                dispose();
            } else {
                new NeuesPasswort();
                dispose();
            }
        });
        einstellungenMenu.add(resetPasswordItem);

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            Session.logout();
            new LoginGUI();
            dispose();
        });
        einstellungenMenu.add(logoutItem);

        menuBar.add(einstellungenMenu);
        setJMenuBar(menuBar);

        panel = new JPanel(new GridBagLayout());
        add(panel);

        label = new JLabel("Quiz Selection");
        baueInhalt();

        panel.setPreferredSize(new Dimension(fixedWidth, panel.getPreferredSize().height));
        pack();
        setLocationRelativeTo(null);
        setMaximumSize(new Dimension(maxWidth, maxHeight));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                aktualisiereFonts();
            }
        });

        setVisible(true);
    }

    /**
     * Aktualisiert den Inhalt des Quiz-Auswahl-Panels.
     * Löscht alle vorhandenen Elemente und baut die Benutzeroberfläche mit den aktuellen Quiz-Dateien neu auf.
     */
    public void refreshContent() {
        panel.removeAll();
        buttons.clear();
        baueInhalt();
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Erstellt den Hauptinhalt der Quiz-Auswahl-Benutzeroberfläche.
     * Durchsucht nach Quiz-Dateien und erstellt Schaltflächen für verfügbare Quizze.
     */
    private void baueInhalt() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(label, gbc);

        String currentUser = Session.getUsername();
        File quizDir = new File("C:/temp/Quiz");
        File[] files = quizDir.listFiles((dir, name) ->
                name.endsWith("." + currentUser + ".txt") || name.endsWith(".Admin.txt"));

        int row = 1;
        if (files != null && files.length > 0) {
            for (File file : files) {
                String fileName = file.getName();
                String suffixUser = "." + currentUser + ".txt";
                String suffixAdmin = ".Admin.txt";
                String buttonLabel = fileName;

                if (fileName.endsWith(suffixUser)) {
                    buttonLabel = fileName.substring(0, fileName.length() - suffixUser.length());
                } else if (fileName.endsWith(suffixAdmin)) {
                    buttonLabel = fileName.substring(0, fileName.length() - suffixAdmin.length());
                }

                JButton quizButton = new JButton(buttonLabel);
                quizButton.setToolTipText("Start Quiz: " + buttonLabel);
                File finalFile = file;
                quizButton.addActionListener(e -> new QuizGUI(finalFile.getName()));
                gbc.gridy = row++;
                panel.add(quizButton, gbc);
                buttons.add(quizButton);
            }
        } else {
            JLabel errorLabel = new JLabel("Keine Quiz-Dateien für Benutzer \"" + currentUser + "\" gefunden.");
            gbc.gridy = row++;
            panel.add(errorLabel, gbc);
        }

        JButton erstellenButton = new JButton("Neues Quiz erstellen");
        erstellenButton.setToolTipText("Öffnet das Fenster zum Erstellen eines neuen Quiz");
        erstellenButton.addActionListener(e -> new QuizErstellenGUI(this));
        gbc.gridy = row++;
        panel.add(erstellenButton, gbc);
        buttons.add(erstellenButton);

        JButton bearbeitenButton = new JButton("Quiz bearbeiten");
        bearbeitenButton.setToolTipText("Öffnet das Fenster zum Bearbeiten eines Quiz");
        bearbeitenButton.addActionListener(e -> new BearbeitenGUI());
        gbc.gridy = row++;
        panel.add(bearbeitenButton, gbc);
        buttons.add(bearbeitenButton);
    }

    /**
     * Aktualisiert die Schriftgrößen basierend auf der aktuellen Fensterbreite.
     * Skaliert die Schriften proportional, um die Lesbarkeit bei verschiedenen Fenstergrößen zu erhalten.
     */
    private void aktualisiereFonts() {
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
