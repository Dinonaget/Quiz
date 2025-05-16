package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class LoginGUI extends JFrame {

    public LoginGUI() {
        setTitle("Quiz-Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        Color hintergrundFarbe = new Color(240, 248, 255);
        Color buttonFarbe = new Color(70, 130, 180);
        Color textFarbe = Color.WHITE;
        Font schrift = new Font("SansSerif", Font.PLAIN, 14);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(hintergrundFarbe);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Benutzername
        JLabel userLabel = new JLabel("Benutzername:");
        userLabel.setFont(schrift);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        userField.setFont(schrift);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(userField, gbc);

        // Passwort
        JLabel passLabel = new JLabel("Passwort:");
        passLabel.setFont(schrift);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(20);
        passField.setFont(schrift);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passField, gbc);

        // Buttons
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, buttonFarbe, textFarbe, schrift);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        JButton registrierButton = new JButton("Registrieren");
        styleButton(registrierButton, new Color(60, 179, 113), textFarbe, schrift);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(registrierButton, gbc);

        JButton passwortVergessenButton = new JButton("Passwort Vergessen");
        styleButton(passwortVergessenButton, new Color(255, 140, 0), textFarbe, schrift);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(passwortVergessenButton, gbc);

        add(panel);
        setVisible(true);

        // Aktionen
        registrierButton.addActionListener((ActionEvent e) -> {
            new Profil_ErstellenGUI();
            dispose();
        });

        loginButton.addActionListener((ActionEvent e) -> {
            String benutzer = userField.getText().trim();
            String passwort = new String(passField.getPassword());

            String gespeichertesPasswort = getBenutzerPasswort(benutzer);
            if (gespeichertesPasswort == null) {
                JOptionPane.showMessageDialog(this, "Benutzer existiert nicht. Bitte registrieren.");
            } else {
                String eingegebenVerschlüsselt = encryptPassword(passwort);
                if (gespeichertesPasswort.equals(eingegebenVerschlüsselt)) {
                    JOptionPane.showMessageDialog(this, "Login erfolgreich!");
                    Session.login(benutzer);
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

    private void styleButton(JButton button, Color background, Color foreground, Font font) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    /**
     * Liest das verschlüsselte Passwort aus der Datei für einen Benutzer.
     */
    private String getBenutzerPasswort(String benutzer) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/temp/Quiz/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 4); // Neues Format
                if (parts.length >= 2 && parts[0].equals(benutzer)) {
                    return parts[1]; // verschlüsseltes Passwort
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verschlüsselt das eingegebene Passwort (muss identisch zu Profil_ErstellenGUI sein).
     */
    private String encryptPassword(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int value = ((c << 5) ^ (c >> 3)) % 256;
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
