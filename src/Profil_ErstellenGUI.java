package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Profil_ErstellenGUI extends JFrame {

    private final Font baseFont = new Font("SansSerif", Font.PLAIN, 14);
    private final java.util.List<JComponent> componentsToResize = new ArrayList<>();

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
        userLabel.setFont(baseFont);
        componentsToResize.add(userLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        userField.setFont(baseFont);
        componentsToResize.add(userField);
        gbc.gridx = 1;
        add(userField, gbc);

        // Passwort
        JLabel pass1Label = new JLabel("Passwort:");
        pass1Label.setFont(baseFont);
        componentsToResize.add(pass1Label);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(pass1Label, gbc);

        JPasswordField pass1Field = new JPasswordField(20);
        pass1Field.setFont(baseFont);
        componentsToResize.add(pass1Field);
        gbc.gridx = 1;
        add(pass1Field, gbc);

        // Passwort bestätigen
        JLabel pass2Label = new JLabel("Passwort bestätigen:");
        pass2Label.setFont(baseFont);
        componentsToResize.add(pass2Label);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(pass2Label, gbc);

        JPasswordField pass2Field = new JPasswordField(20);
        pass2Field.setFont(baseFont);
        componentsToResize.add(pass2Field);
        gbc.gridx = 1;
        add(pass2Field, gbc);

        // Sicherheitsfrage
        JLabel questionLabel = new JLabel("Sicherheitsfrage:");
        questionLabel.setFont(baseFont);
        componentsToResize.add(questionLabel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(questionLabel, gbc);

        JTextField questionField = new JTextField(20);
        questionField.setFont(baseFont);
        componentsToResize.add(questionField);
        gbc.gridx = 1;
        add(questionField, gbc);

        // Sicherheitsantwort
        JLabel answerLabel = new JLabel("Antwort:");
        answerLabel.setFont(baseFont);
        componentsToResize.add(answerLabel);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(answerLabel, gbc);

        JTextField answerField = new JTextField(20);
        answerField.setFont(baseFont);
        componentsToResize.add(answerField);
        gbc.gridx = 1;
        add(answerField, gbc);

        // Bestätigen
        JButton confirmButton = new JButton("Bestätigen");
        confirmButton.setFont(baseFont);
        componentsToResize.add(confirmButton);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        add(confirmButton, gbc);

        setVisible(true);

        // Fenstergrößenänderung => dynamische Schriftanpassung
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int fontSize = Math.max(12, width / 40); // Skalierung
                Font resizedFont = new Font("SansSerif", Font.PLAIN, fontSize);
                for (JComponent comp : componentsToResize) {
                    comp.setFont(resizedFont);
                }
            }
        });

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
            String encryptedAnswer = encryptAnswer(answer);

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

    public static void writeUser(String filename, String username, String password, String question, String encryptedAnswer) throws IOException {
        new File("C:/temp/Quiz").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(username + ":" + password + ":" + question + ":" + encryptedAnswer);
            writer.newLine();
        }
    }

    public static String encryptPassword(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int value = ((c << 5) ^ (c >> 3)) % 256;
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    public static String encryptAnswer(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int value = ((c * 7) + 13) % 256;
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Profil_ErstellenGUI::new);
    }
}
