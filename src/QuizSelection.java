package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class QuizSelection extends JFrame {

    public QuizSelection() {
        setTitle("Quiz Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel label = new JLabel("Quiz Auswahl");
        label.setFont(new Font("Arial", Font.BOLD, 15));
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        label.setBorder(border);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        // Benutzername aus der Session
        String currentUser = Session.getUsername();

        File quizDir = new File("C:/temp/Quiz");
        File[] files = quizDir.listFiles((dir, name) ->
                name.endsWith("." + currentUser + ".txt") || name.endsWith(".Admin.txt"));

        int row = 1;

        if (files != null && files.length > 0) {
            for (File file : files) {
                String fileName = file.getName();
                // Entferne .username.txt für den Button-Text
                String buttonLabel = fileName.substring(0, fileName.lastIndexOf('.' + currentUser));

                JButton quizButton = new JButton(buttonLabel);
                quizButton.setToolTipText("Start Quiz: " + buttonLabel);

                quizButton.addActionListener((ActionEvent e) -> {
                    new QuizGUI(file.getName()); // z.B. "mathe.benutzer.txt"
                });

                gbc.gridx = 0;
                gbc.gridy = row++;
                panel.add(quizButton, gbc);
            }
        } else {
            JLabel errorLabel = new JLabel("Keine Quiz-Dateien für Benutzer \"" + currentUser + "\" gefunden.");
            gbc.gridx = 0;
            gbc.gridy = row++;
            panel.add(errorLabel, gbc);
        }

        // Button zum Erstellen eines neuen Quiz
        JButton erstellenButton = new JButton("Neues Quiz erstellen");
        erstellenButton.setToolTipText("Öffnet das Fenster zum Erstellen eines neuen Quiz");

        erstellenButton.addActionListener(e -> {
            new QuizErstellenGUI();
        });

        gbc.gridx = 0;
        gbc.gridy = row++;
        panel.add(erstellenButton, gbc);

        add(panel);
        setResizable(false);
        setVisible(true);
    }
}