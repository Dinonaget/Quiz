package src;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;

/**
 * LoginGUI class creates the login interface for the Quiz application.
 * Provides user authentication functionality with dynamic font scaling.
 */
public class LoginGUI extends JFrame {

    /** Base font used for all GUI components */
    private Font baseFont = new Font("SansSerif", Font.PLAIN, 14);

    /** GUI components for user interface */
    private JLabel userLabel, passLabel;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton, registrierButton, passwortVergessenButton;

    /**
     * Constructor that initializes and displays the login GUI.
     * Sets up all components, event listeners, and dynamic font scaling.
     */
    public LoginGUI() {
        // Configure main window properties
        setTitle("Quiz-Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Create main panel with GridBagLayout for flexible component positioning
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username input section
        userLabel = new JLabel("Benutzername:");
        userLabel.setFont(baseFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        userField = new JTextField(20);
        userField.setFont(baseFont);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(userField, gbc);

        // Password input section
        passLabel = new JLabel("Passwort:");
        passLabel.setFont(baseFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);

        passField = new JPasswordField(20);
        passField.setFont(baseFont);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passField, gbc);

        // Button section
        loginButton = new JButton("Login");
        loginButton.setFont(baseFont);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        registrierButton = new JButton("Registrieren");
        registrierButton.setFont(baseFont);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(registrierButton, gbc);

        passwortVergessenButton = new JButton("Passwort Vergessen");
        passwortVergessenButton.setFont(baseFont);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(passwortVergessenButton, gbc);

        // Add panel to frame and set login button as default
        add(panel);
        panel.getRootPane().setDefaultButton(loginButton);

        setVisible(true);

        // Dynamic font scaling based on window size
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int fontSize = Math.max(12, width / 40); // Scale font size with minimum of 12
                Font resizedFont = new Font("SansSerif", Font.PLAIN, fontSize);

                // Apply scaled font to all components
                userLabel.setFont(resizedFont);
                passLabel.setFont(resizedFont);
                userField.setFont(resizedFont);
                passField.setFont(resizedFont);
                loginButton.setFont(resizedFont);
                registrierButton.setFont(resizedFont);
                passwortVergessenButton.setFont(resizedFont);
            }
        });

        // Event handlers for buttons

        // Registration button - opens profile creation GUI
        registrierButton.addActionListener((ActionEvent e) -> {
            new Profil_ErstellenGUI();
            dispose();
        });

        // Login button - handles user authentication
        loginButton.addActionListener((ActionEvent e) -> {
            String benutzer = userField.getText().trim();
            String passwort = new String(passField.getPassword());

            // Retrieve stored password for the user
            String gespeichertesPasswort = getBenutzerPasswort(benutzer);
            if (gespeichertesPasswort == null) {
                // User doesn't exist
                JOptionPane.showMessageDialog(this, "Benutzer existiert nicht. Bitte registrieren.");
                userField.setBorder(new LineBorder(Color.RED, 1));
                passField.setBorder(new LineBorder(Color.RED, 1));
            } else {
                // Verify password
                String eingegebenVerschluesselt = encryptPassword(passwort);
                if (gespeichertesPasswort.equals(eingegebenVerschluesselt)) {
                    // Successful login
                    Session.login(benutzer);
                    new QuizSelection();
                    dispose();
                } else {
                    // Wrong password
                    JOptionPane.showMessageDialog(null, "Falsches Passwort!");
                    passField.setBorder(new LineBorder(Color.RED, 1));
                }
            }
        });

        // Forgot password button - opens password reset GUI
        passwortVergessenButton.addActionListener((ActionEvent e) -> {
            new NeuesPasswort();
            dispose();
        });
    }

    /**
     * Styles a button with specified colors and font.
     * Currently unused but provides button styling functionality.
     *
     * @param button The button to style
     * @param background Background color
     * @param foreground Text color
     * @param font Font to apply
     */
    private void styleButton(JButton button, Color background, Color foreground, Font font) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    /**
     * Retrieves the stored password for a given username from the users file.
     *
     * @param benutzer The username to look up
     * @return The encrypted password if user exists, null otherwise
     */
    private String getBenutzerPasswort(String benutzer) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/temp/Quiz/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse user data (format: username:password:...)
                String[] parts = line.split(":", 4);
                if (parts.length >= 2 && parts[0].equals(benutzer)) {
                    return parts[1]; // Return encrypted password
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }

    /**
     * Encrypts a password using a simple bit-shifting algorithm.
     * Uses XOR and bit shifting operations for basic encryption.
     *
     * @param text The plain text password to encrypt
     * @return The encrypted password as a space-separated string of numbers
     */
    private String encryptPassword(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            // Apply bit shifting and XOR operations
            int value = ((c << 5) ^ (c >> 3)) % 256;
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    /**
     * Main method for standalone testing of the LoginGUI.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}