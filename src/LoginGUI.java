package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import org.json.JSONObject;

public class LoginGUI extends JFrame {

    public LoginGUI() {
        // Titel setzen
        setTitle("Quiz-Login");

        // Grundeinstellungen für das Fenster
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Fenster zentrieren

//        // Farben und Schrift
//        Color hintergrundFarbe = new Color(240, 248, 255); // leichtes Blau
//        Color buttonFarbe = new Color(70, 130, 180);       // Stahlblau
//        Color textFarbe = Color.WHITE;
//        Font schrift = new Font("SansSerif", Font.PLAIN, 14);

        // Hauptpanel mit Hintergrundfarbe
        JPanel panel = new JPanel(new GridBagLayout());
//        panel.setBackground(hintergrundFarbe);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Benutzername Label
        JLabel userLabel = new JLabel("Benutzername:");
//        userLabel.setFont(schrift);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        // Benutzername Textfeld
        JTextField userField = new JTextField(20);
//        userField.setFont(schrift);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(userField, gbc);

        // Passwort Label
        JLabel passLabel = new JLabel("Passwort:");
//        passLabel.setFont(schrift);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);

        // Passwort-Feld
        JPasswordField passField = new JPasswordField(20);
//        passField.setFont(schrift);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passField, gbc);

        // Login-Button
        JButton loginButton = new JButton("Login");
//        styleButton(loginButton, buttonFarbe, textFarbe, schrift);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        getRootPane().setDefaultButton(loginButton);
        panel.add(loginButton, gbc);

        // Registrieren-Button
        JButton registrierButton = new JButton("Registrieren");
//        styleButton(registrierButton, new Color(60, 179, 113), textFarbe, schrift); // Mittelgrün
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(registrierButton, gbc);

        // Passwort-Vergessen-Button
        JButton passwortVergessenButton = new JButton("Passwort Vergessen");
//        styleButton(passwortVergessenButton, new Color(255, 140, 0), textFarbe, schrift); // Orange
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(passwortVergessenButton, gbc);

        // Panel zum Frame hinzufügen
        add(panel);

        // Sichtbar machen
        setVisible(true);

        // ActionListener für den Registrieren-Button
        registrierButton.addActionListener((ActionEvent e) -> {
            new Profil_ErstellenGUI();
            dispose();
        });

        // ActionListener Login
        loginButton.addActionListener((ActionEvent e) -> {
            String benutzer = userField.getText();
            String passwort = new String(passField.getPassword());

            // Benutzerdaten aus der users.txt-Datei lesen
            String gespeichertesPasswort = getBenutzerPasswort(benutzer);
            if (gespeichertesPasswort == null) {
                JOptionPane.showMessageDialog(this, "Benutzer existiert nicht. Bitte registrieren.");
            } else {
                String entschlüsseltesPasswort = decryptPassword(gespeichertesPasswort);
                if (entschlüsseltesPasswort.equals(passwort)) {
                    JOptionPane.showMessageDialog(this, "Login erfolgreich!");
                    new QuizSelection();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Falsches Passwort!");
                }
            }
        });

        passwortVergessenButton.addActionListener((ActionEvent e) -> {
            new NeuesPasswort();
            dispose();
        });
    }

    // Hilfsmethode zur Button-Gestaltung
    private void styleButton(JButton button, Color background, Color foreground, Font font) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    /**
     * Liest das verschlüsselte Passwort eines Benutzers aus der users.txt-Datei.
     */
    private String getBenutzerPasswort(String benutzer) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/temp/Quiz/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];
                    if (username.equals(benutzer)) {
                        return password;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Entschlüsselt das Passwort, das aus der users.txt-Datei gelesen wurde.
     */
    private String decryptPassword(String encryptedPassword) {
        StringBuilder decryptedPassword = new StringBuilder();
        String[] values = encryptedPassword.split(" ");
        for (String value : values) {
            int decodedValue = Integer.parseInt(value);
            decodedValue = ((decodedValue >> 5) ^ (decodedValue << 3)) % 256; // Entschlüsselungs-Logik
            decryptedPassword.append((char) decodedValue);
        }
        return decryptedPassword.toString();
    }

    // Einstiegspunkt
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
