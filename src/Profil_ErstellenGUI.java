package src;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * GUI class for creating user profiles with username, password, and security question.
 * Provides a form with dynamic font scaling based on window size.
 */
public class Profil_ErstellenGUI extends JFrame {

    /** Base font used for all GUI components */
    private final Font baseFont = new Font("SansSerif", Font.PLAIN, 14);

    /** List of components that will be resized when window size changes */
    private final java.util.List<JComponent> componentsToResize = new ArrayList<>();

    /**
     * Constructor that initializes and displays the profile creation GUI.
     * Sets up all form fields, buttons, and event listeners.
     */
    public Profil_ErstellenGUI() {
        setTitle("Profil erstellen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field
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

        // Password field
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

        // Password confirmation field
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

        // Security question field
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

        // Security answer field
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

        // Confirm button
        JButton confirmButton = new JButton("Bestätigen");
        confirmButton.setFont(baseFont);
        componentsToResize.add(confirmButton);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        add(confirmButton, gbc);
        getRootPane().setDefaultButton(confirmButton);
        setVisible(true);

        // Component listener for dynamic font scaling on window resize
        addComponentListener(new ComponentAdapter() {
            /**
             * Adjusts font size of all components when window is resized.
             * Font size scales based on window width.
             *
             * @param e the component event triggered by window resize
             */
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int fontSize = Math.max(12, width / 40);
                Font resizedFont = new Font("SansSerif", Font.PLAIN, fontSize);
                for (JComponent comp : componentsToResize) {
                    comp.setFont(resizedFont);
                }
            }
        });

        // Action listener for confirm button
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
                JOptionPane.showMessageDialog(null,"Passwort stimmt nicht Überein!");
                pass1Field.setBorder(new LineBorder(Color.RED, 1));
                pass2Field.setBorder(new LineBorder(Color.RED, 1));
                return;
            }

            String encryptedPassword = encryptPassword(pass1);
            String encryptedAnswer = encryptAnswer(answer);

            try {
                writeUser("C:/temp/Quiz/users.txt", username, encryptedPassword, question, encryptedAnswer);
                new LoginGUI();
                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern!");
            }
        });
    }

    /**
     * Writes user data to a file in colon-separated format.
     * Creates the directory structure if it doesn't exist.
     *
     * @param filename the path to the file where user data will be stored
     * @param username the username to be saved
     * @param password the encrypted password
     * @param question the security question
     * @param encryptedAnswer the encrypted security answer
     * @throws IOException if an error occurs during file writing
     */
    public static void writeUser(String filename, String username, String password, String question, String encryptedAnswer) throws IOException {
        new File("C:/temp/Quiz").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(username + ":" + password + ":" + question + ":" + encryptedAnswer);
            writer.newLine();
        }
    }

    /**
     * Encrypts a password using a custom bit manipulation algorithm.
     * Each character is processed with left shift, right shift, and XOR operations.
     *
     * @param text the password text to encrypt
     * @return the encrypted password as space-separated numeric values
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
     * Encrypts an answer using a simple mathematical transformation.
     * Each character is multiplied by 7, added to 13, and modulo 256 is applied.
     *
     * @param text the answer text to encrypt
     * @return the encrypted answer as space-separated numeric values
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
     * Main method that launches the profile creation GUI.
     * Uses SwingUtilities.invokeLater for thread safety.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Profil_ErstellenGUI::new);
    }
}