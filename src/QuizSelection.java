package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class QuizSelection extends JFrame {
    public QuizSelection() {
        JFrame frame = new JFrame("Quiz Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel label = new JLabel("Quiz Selection");
        label.setFont(new Font("Arial", Font.BOLD, 15));
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        label.setBorder(border);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        // Dateien im Verzeichnis C:/temp/Quiz
        File quizDir = new File("C:/temp/Quiz");
        File[] files = quizDir.listFiles((dir, name) -> name.endsWith(".txt") && !name.equalsIgnoreCase("users.txt"));

        int row = 1;

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String buttonLabel = fileName.substring(0, fileName.length() - 4); // ohne ".txt"

                JButton quizButton = new JButton(buttonLabel);
                quizButton.setToolTipText("Start Quiz: " + buttonLabel);

                quizButton.addActionListener((ActionEvent e) -> {
                    QuizGUI quizGUI = new QuizGUI(); // Optional: Datei übergeben
                });

                gbc.gridx = 0;
                gbc.gridy = row++;
                panel.add(quizButton, gbc);
            }
        } else {
            JLabel errorLabel = new JLabel("Fehler beim Laden der Quiz-Dateien.");
            gbc.gridx = 0;
            gbc.gridy = row++;
            panel.add(errorLabel, gbc);
        }

        // Neuen Button zum Quiz erstellen
        JButton erstellenButton = new JButton("Neues Quiz erstellen");
        erstellenButton.setToolTipText("Öffnet das Fenster zum Erstellen eines neuen Quiz");

        erstellenButton.addActionListener(e -> {
            new QuizErstellenGUI();
        });

        gbc.gridx = 0;
        gbc.gridy = row++;
        panel.add(erstellenButton, gbc);

        frame.add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private QuizSelection getQuizSelection() {
        return this;
    }
}
