package src;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;

public class LoginGUI extends JFrame {

    private Font baseFont = new Font("SansSerif", Font.PLAIN, 14);
    private JLabel userLabel, passLabel;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton, registrierButton, passwortVergessenButton;

    public LoginGUI() {
        setTitle("Quiz-Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        // Benutzername
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

        // Passwort
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

        // Buttons
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

        add(panel);
        panel.getRootPane().setDefaultButton(loginButton);

        setVisible(true);

        // Fenstergrößenänderung => dynamisch Schriftgrößen anpassen
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int fontSize = Math.max(12, width / 40); // Skaliere Schriftgröße
                Font resizedFont = new Font("SansSerif", Font.PLAIN, fontSize);
                userLabel.setFont(resizedFont);
                passLabel.setFont(resizedFont);
                userField.setFont(resizedFont);
                passField.setFont(resizedFont);
                loginButton.setFont(resizedFont);
                registrierButton.setFont(resizedFont);
                passwortVergessenButton.setFont(resizedFont);
            }
        });

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
                userField.setBorder(new LineBorder(Color.RED, 1));
                passField.setBorder(new LineBorder(Color.RED, 1));
            } else {
                String eingegebenVerschluesselt = encryptPassword(passwort);
                if (gespeichertesPasswort.equals(eingegebenVerschluesselt)) {
                    Session.login(benutzer);
                    new QuizSelection();
                    dispose();
                } else {

                    JOptionPane.showMessageDialog(null, "Falsches Passwort!");
                    passField.setBorder(new LineBorder(Color.RED, 1));
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

    private String getBenutzerPasswort(String benutzer) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/temp/Quiz/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 4);
                if (parts.length >= 2 && parts[0].equals(benutzer)) {
                    return parts[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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
