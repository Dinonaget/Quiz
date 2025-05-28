package src;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse NeuesPasswort ermöglicht das Zurücksetzen des Passworts eines Benutzers.
 * Sie bietet zwei Konstruktoren: einen für das Zurücksetzen mit Sicherheitsfrage und einen für angemeldete Benutzer.
 */
public class NeuesPasswort extends JFrame {

    /**
     * Konstruktor für das Zurücksetzen des Passworts mit Sicherheitsfrage.
     */
    public NeuesPasswort() {
        setTitle("Passwort zurücksetzen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
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
        gbc.gridy = 0;
        add(userField, gbc);

        // Sicherheitsfrage
        JLabel questionLabel = new JLabel("Sicherheitsfrage:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(questionLabel, gbc);

        JTextField questionField = new JTextField(20);
        questionField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(questionField, gbc);

        // Antwort
        JLabel answerLabel = new JLabel("Antwort:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(answerLabel, gbc);

        JTextField answerField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(answerField, gbc);

        // Neues Passwort
        JLabel pass1Label = new JLabel("Neues Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(pass1Label, gbc);

        JPasswordField pass1Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(pass1Field, gbc);

        // Passwort bestätigen
        JLabel pass2Label = new JLabel("Passwort bestätigen:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(pass2Label, gbc);

        JPasswordField pass2Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(pass2Field, gbc);

        // Button
        JButton confirmButton = new JButton("Zurücksetzen");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        add(confirmButton, gbc);

        setVisible(true);

        // Sicherheitsfrage beim Username-Eingabe
        userField.addActionListener(e -> {
            String username = userField.getText().trim();
            String question = getSecurityQuestion("C:/temp/Quiz/users.txt", username);
            if (question != null) {
                questionField.setText(question);
            } else {
                JOptionPane.showMessageDialog(this, "Benutzer nicht gefunden!");
            }
        });

        confirmButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String answer = answerField.getText().trim();
            String newPass1 = new String(pass1Field.getPassword());
            String newPass2 = new String(pass2Field.getPassword());

            if (username.isEmpty() || answer.isEmpty() || newPass1.isEmpty() || newPass2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alle Felder müssen ausgefüllt sein!");
                return;
            }

            if (!newPass1.equals(newPass2)) {
                JOptionPane.showMessageDialog(this, "Passwörter stimmen nicht überein!");
                return;
            }

            try {
                boolean success = resetPasswordWithSecurityCheck(
                        "C:/temp/Quiz/users.txt",
                        username,
                        answer,
                        encryptPassword(newPass1)
                );

                if (success) {
                    JOptionPane.showMessageDialog(this, "Passwort erfolgreich geändert.");
                    new QuizSelection();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Sicherheitsantwort falsch oder Benutzer nicht gefunden.");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern!");
            }
        });
    }

    /**
     * Konstruktor für das Zurücksetzen des Passworts für einen angemeldeten Benutzer.
     *
     * @param username Der Benutzername des angemeldeten Benutzers.
     */
    public NeuesPasswort(String username) {
        setTitle("Passwort zurücksetzen für " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Neues Passwort
        JLabel pass1Label = new JLabel("Neues Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(pass1Label, gbc);

        JPasswordField pass1Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(pass1Field, gbc);

        // Passwort bestätigen
        JLabel pass2Label = new JLabel("Passwort bestätigen:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(pass2Label, gbc);

        JPasswordField pass2Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(pass2Field, gbc);

        // Button
        JButton confirmButton = new JButton("Zurücksetzen");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(confirmButton, gbc);

        setVisible(true);

        confirmButton.addActionListener(e -> {
            String newPass1 = new String(pass1Field.getPassword());
            String newPass2 = new String(pass2Field.getPassword());

            if (newPass1.isEmpty() || newPass2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alle Felder müssen ausgefüllt sein!");
                return;
            }

            if (!newPass1.equals(newPass2)) {
                JOptionPane.showMessageDialog(this, "Passwörter stimmen nicht überein!");
                return;
            }

            try {
                boolean success = resetPasswordWithoutSecurityCheck(
                        "C:/temp/Quiz/users.txt",
                        username,
                        encryptPassword(newPass1)
                );

                if (success) {
                    JOptionPane.showMessageDialog(this, "Passwort erfolgreich geändert.");
                    new QuizSelection();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Fehler: Benutzer nicht gefunden.");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern!");
            }
        });
    }

    /**
     * Setzt das Passwort eines Benutzers zurück, nachdem die Sicherheitsfrage überprüft wurde.
     *
     * @param filename Der Pfad zur Benutzerdatei.
     * @param username Der Benutzername.
     * @param answer Die Antwort auf die Sicherheitsfrage.
     * @param newEncryptedPassword Das neue verschlüsselte Passwort.
     * @return true, wenn das Passwort erfolgreich zurückgesetzt wurde, sonst false.
     * @throws IOException Wenn ein Fehler beim Lesen oder Schreiben der Datei auftritt.
     */
    public static boolean resetPasswordWithSecurityCheck(String filename, String username, String answer, String newEncryptedPassword) throws IOException {
        if (!validateUserFile(filename)) return false;

        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<String> updatedLines = new ArrayList<>();
        boolean userFound = false;

        for (String line : lines) {
            String[] parts = line.split(":", 4);
            if (parts.length == 4 && parts[0].equals(username)) {
                String storedEncryptedAnswer = parts[3].trim();
                String givenEncryptedAnswer = encryptAnswer(answer);
                if (storedEncryptedAnswer.equals(givenEncryptedAnswer)) {
                    updatedLines.add(username + ":" + newEncryptedPassword + ":" + parts[2] + ":" + storedEncryptedAnswer);
                    userFound = true;
                } else {
                    updatedLines.add(line);
                }
            } else {
                updatedLines.add(line);
            }
        }

        if (userFound) {
            Files.write(Paths.get(filename), updatedLines);
        }

        return userFound;
    }

    /**
     * Setzt das Passwort eines Benutzers zurück, ohne die Sicherheitsfrage zu überprüfen.
     *
     * @param filename Der Pfad zur Benutzerdatei.
     * @param username Der Benutzername.
     * @param newEncryptedPassword Das neue verschlüsselte Passwort.
     * @return true, wenn das Passwort erfolgreich zurückgesetzt wurde, sonst false.
     * @throws IOException Wenn ein Fehler beim Lesen oder Schreiben der Datei auftritt.
     */
    public static boolean resetPasswordWithoutSecurityCheck(String filename, String username, String newEncryptedPassword) throws IOException {
        if (!validateUserFile(filename)) return false;

        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<String> updatedLines = new ArrayList<>();
        boolean userFound = false;

        for (String line : lines) {
            String[] parts = line.split(":", 4);
            if (parts.length == 4 && parts[0].equals(username)) {
                updatedLines.add(username + ":" + newEncryptedPassword + ":" + parts[2] + ":" + parts[3]);
                userFound = true;
            } else {
                updatedLines.add(line);
            }
        }

        if (userFound) {
            Files.write(Paths.get(filename), updatedLines);
        }

        return userFound;
    }

    /**
     * Überprüft die Benutzerdatei auf Existenz, Lesbarkeit und korrekte Formatierung.
     *
     * @param filename Der Pfad zur Benutzerdatei.
     * @return true, wenn die Datei gültig ist, sonst false.
     */
    public static boolean validateUserFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "Benutzerdaten-Datei existiert nicht: " + filename);
            return false;
        }
        if (!file.canRead()) {
            JOptionPane.showMessageDialog(null, "Benutzerdaten-Datei kann nicht gelesen werden: " + filename);
            return false;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(":", 4);
                if (parts.length != 4) {
                    JOptionPane.showMessageDialog(null, "Ungültige Zeile in Benutzerdaten-Datei:\n" + line);
                    return false;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Lesen der Datei:\n" + e.getMessage());
            return false;
        }

        return true;
    }


    /**
     * Gibt die Sicherheitsfrage eines Benutzers zurück.
     *
     * @param filename Der Pfad zur Benutzerdatei.
     * @param username Der Benutzername.
     * @return Die Sicherheitsfrage des Benutzers oder null, wenn der Benutzer nicht gefunden wurde.
     */
    public static String getSecurityQuestion(String filename, String username) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                String[] parts = line.split(":", 4);
                if (parts.length == 4 && parts[0].equals(username)) {
                    return parts[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verschlüsselt ein Passwort.
     *
     * @param text Das zu verschlüsselnde Passwort.
     * @return Das verschlüsselte Passwort.
     */
    public static String encryptPassword(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int value = ((c << 5) ^ (c >> 3)) % 256;
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    /**
     * Verschlüsselt eine Antwort auf eine Sicherheitsfrage.
     *
     * @param text Die zu verschlüsselnde Antwort.
     * @return Die verschlüsselte Antwort.
     */
    public static String encryptAnswer(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int value = ((c * 7) + 13) % 256;
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    /**
     * Die Hauptmethode zum Starten der Anwendung.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NeuesPasswort());
    }
}
