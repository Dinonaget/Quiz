package src;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI class for resetting user passwords with two different modes:
 * 1. Password reset using security question for unknown users
 * 2. Direct password reset for already logged-in users
 */
public class NeuesPasswort extends JFrame {

    /**
     * Default constructor for resetting password with security question verification.
     * Creates a form with username, security question, answer, and new password fields.
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

        // Username input field
        JLabel userLabel = new JLabel("Benutzername:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userField, gbc);

        // Security question display field (read-only)
        JLabel questionLabel = new JLabel("Sicherheitsfrage:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(questionLabel, gbc);

        JTextField questionField = new JTextField(20);
        questionField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(questionField, gbc);

        // Security question answer input field
        JLabel answerLabel = new JLabel("Antwort:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(answerLabel, gbc);

        JTextField answerField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(answerField, gbc);

        // New password input field
        JLabel pass1Label = new JLabel("Neues Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(pass1Label, gbc);

        JPasswordField pass1Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(pass1Field, gbc);

        // Password confirmation input field
        JLabel pass2Label = new JLabel("Passwort bestätigen:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(pass2Label, gbc);

        JPasswordField pass2Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(pass2Field, gbc);

        // Reset button
        JButton confirmButton = new JButton("Zurücksetzen");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        add(confirmButton, gbc);
        setVisible(true);

        // Load security question when username is entered
        userField.addActionListener(e -> {
            String username = userField.getText().trim();
            String question = getSecurityQuestion("C:/temp/Quiz/users.txt", username);
            if (question != null) {
                questionField.setText(question);
            } else {
                JOptionPane.showMessageDialog(this, "Benutzer nicht gefunden!");
            }
        });

        // Handle password reset confirmation
        confirmButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String answer = answerField.getText().trim();
            String newPass1 = new String(pass1Field.getPassword());
            String newPass2 = new String(pass2Field.getPassword());

            // Validate all required fields are filled
            if (username.isEmpty() || answer.isEmpty() || newPass1.isEmpty() || newPass2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alle Felder müssen ausgefüllt sein!");
                return;
            }

            // Verify password confirmation matches
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
     * Constructor for resetting password for an already logged-in user.
     * Creates a simplified form with only password and confirmation fields.
     *
     * @param username the username of the currently logged-in user
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

        // New password input field
        JLabel pass1Label = new JLabel("Neues Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(pass1Label, gbc);

        JPasswordField pass1Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(pass1Field, gbc);

        // Password confirmation input field
        JLabel pass2Label = new JLabel("Passwort bestätigen:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(pass2Label, gbc);

        JPasswordField pass2Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(pass2Field, gbc);

        // Reset button
        JButton confirmButton = new JButton("Zurücksetzen");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(confirmButton, gbc);
        getRootPane().setDefaultButton(confirmButton);
        setVisible(true);

        // Handle password reset for logged-in user
        confirmButton.addActionListener(e -> {
            String newPass1 = new String(pass1Field.getPassword());
            String newPass2 = new String(pass2Field.getPassword());

            // Validate both password fields are filled
            if (newPass1.isEmpty() || newPass2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alle Felder müssen ausgefüllt sein!");
                return;
            }

            // Verify password confirmation matches
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
     * Resets a user's password after verifying their security question answer.
     *
     * @param filename the path to the user data file
     * @param username the username whose password should be reset
     * @param answer the user's answer to their security question
     * @param newEncryptedPassword the new encrypted password to set
     * @return true if the password was successfully reset, false otherwise
     * @throws IOException if there's an error reading or writing the file
     */
    public static boolean resetPasswordWithSecurityCheck(String filename, String username, String answer, String newEncryptedPassword) throws IOException {
        if (!validateUserFile(filename)) return false;

        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<String> updatedLines = new ArrayList<>();
        boolean userFound = false;

        // Process each line in the user file
        for (String line : lines) {
            String[] parts = line.split(":", 4);
            if (parts.length == 4 && parts[0].equals(username)) {
                String storedEncryptedAnswer = parts[3].trim();
                String givenEncryptedAnswer = encryptAnswer(answer);
                // Verify the security answer matches
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

        // Write updated data back to file if user was found and updated
        if (userFound) {
            Files.write(Paths.get(filename), updatedLines);
        }

        return userFound;
    }

    /**
     * Resets a user's password without security question verification (for logged-in users).
     *
     * @param filename the path to the user data file
     * @param username the username whose password should be reset
     * @param newEncryptedPassword the new encrypted password to set
     * @return true if the password was successfully reset, false otherwise
     * @throws IOException if there's an error reading or writing the file
     */
    public static boolean resetPasswordWithoutSecurityCheck(String filename, String username, String newEncryptedPassword) throws IOException {
        if (!validateUserFile(filename)) return false;

        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<String> updatedLines = new ArrayList<>();
        boolean userFound = false;

        // Process each line in the user file
        for (String line : lines) {
            String[] parts = line.split(":", 4);
            if (parts.length == 4 && parts[0].equals(username)) {
                // Update password while keeping other data unchanged
                updatedLines.add(username + ":" + newEncryptedPassword + ":" + parts[2] + ":" + parts[3]);
                userFound = true;
            } else {
                updatedLines.add(line);
            }
        }

        // Write updated data back to file if user was found and updated
        if (userFound) {
            Files.write(Paths.get(filename), updatedLines);
        }

        return userFound;
    }

    /**
     * Validates the user data file format and accessibility.
     *
     * @param filename the path to the user data file to validate
     * @return true if the file is valid and accessible, false otherwise
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
            // Validate each line has the correct format (4 colon-separated parts)
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
     * Retrieves the security question for a specific username from the user file.
     *
     * @param filename the path to the user data file
     * @param username the username to look up
     * @return the security question for the user, or null if user not found
     */
    public static String getSecurityQuestion(String filename, String username) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                String[] parts = line.split(":", 4);
                if (parts.length == 4 && parts[0].equals(username)) {
                    return parts[2]; // Security question is the third part
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypts a password using a bitwise encryption algorithm.
     *
     * @param text the plain text password to encrypt
     * @return the encrypted password as a space-separated string of numbers
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
     * Encrypts a security question answer using a mathematical encryption algorithm.
     *
     * @param text the plain text answer to encrypt
     * @return the encrypted answer as a space-separated string of numbers
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
     * Main method to launch the password reset application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NeuesPasswort());
    }
}