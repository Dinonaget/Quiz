package src;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Profil_ErstellenGUI extends JFrame {

    public Profil_ErstellenGUI() {
        setTitle("Profil erstellen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Benutzername
        JLabel userLabel = new JLabel("Benutzername:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        gbc.gridx = 1;
        add(userField, gbc);

        // Passwort
        JLabel pass1Label = new JLabel("Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(pass1Label, gbc);

        JPasswordField pass1Field = new JPasswordField(20);
        gbc.gridx = 1;
        add(pass1Field, gbc);

        // Passwort bestätigen
        JLabel pass2Label = new JLabel("Passwort bestätigen:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(pass2Label, gbc);

        JPasswordField pass2Field = new JPasswordField(20);
        gbc.gridx = 1;
        add(pass2Field, gbc);

        // Sicherheitsfrage
        JLabel questionLabel = new JLabel("Sicherheitsfrage:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(questionLabel, gbc);

        JTextField questionField = new JTextField(20);
        gbc.gridx = 1;
        add(questionField, gbc);

        // Sicherheitsantwort
        JLabel answerLabel = new JLabel("Antwort:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(answerLabel, gbc);

        JTextField answerField = new JTextField(20);
        gbc.gridx = 1;
        add(answerField, gbc);

        // Bestätigen
        JButton confirmButton = new JButton("Bestätigen");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        getRootPane().setDefaultButton(confirmButton);
        add(confirmButton, gbc);

        setVisible(true);

        // Aktion bei Button-Klick
        confirmButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String pass1 = new String(pass1Field.getPassword());
            String pass2 = new String(pass2Field.getPassword());
            String question = questionField.getText().trim();
            String answer = answerField.getText().trim();

            if (username.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || question.isEmpty() || answer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alle Felder müssen ausgefüllt sein!");
                return;
            }

            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(this, "Passwörter müssen übereinstimmen!");
                return;
            }

            String encryptedPassword = encryptPassword(pass1);
            String encryptedAnswer = encryptAnswer(answer); // Neue Verschlüsselung für Antwort

            try {
                writeUser("C:/temp/Quiz/users.txt", username, encryptedPassword, question, encryptedAnswer);
                new QuizSelection();
                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern!");
            }
        });
    }

    /**
     * Schreibt Nutzerdaten in Datei im Format:
     * benutzername:passwort:sicherheitsfrage:antwort
     */
    public static void writeUser(String filename, String username, String password, String question, String encryptedAnswer) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(username + ":" + password + ":" + question + ":" + encryptedAnswer);
            writer.newLine();
        }
    }

    // Passwort-Verschlüsselung (gleich wie bisher)
    public static String encryptPassword(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int value = ((c << 5) ^ (c >> 3)) % 256;
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    // Sicherheitsantwort-Verschlüsselung (NEUE METHODE)
    public static String encryptAnswer(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int value = ((c * 7) + 13) % 256; // andere Rechnung
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Profil_ErstellenGUI::new);
    }
}
