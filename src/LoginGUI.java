package src;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;

/**
 * Die LoginGUI-Klasse erstellt die Login-Schnittstelle für die Quiz-Anwendung.
 * Bietet Benutzerauthentifizierungsfunktionalität mit dynamischer Schriftgrößenanpassung.
 */
public class LoginGUI extends JFrame {

    /** Basis-Schriftart, die für alle GUI-Komponenten verwendet wird */
    private Font baseFont = new Font("SansSerif", Font.PLAIN, 14);

    /** GUI-Komponenten für die Benutzeroberfläche */
    private JLabel userLabel, passLabel;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton, registrierButton, passwortVergessenButton;

    /**
     * Konstruktor, der die Login-GUI initialisiert und anzeigt.
     * Richtet alle Komponenten, Event-Listener und dynamische Schriftgrößenanpassung ein.
     */
    public LoginGUI() {
        // Konfiguriere die Haupfenstereigenschaften
        setTitle("Quiz-Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Erstelle das Hauptpanel mit GridBagLayout für flexible Komponentenpositionierung
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Benutzername-Eingabebereich
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

        // Passwort-Eingabebereich
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

        // Schaltflächenbereich
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

        // Füge das Panel zum Frame hinzu und setze die Login-Schaltfläche als Standard
        add(panel);
        panel.getRootPane().setDefaultButton(loginButton);

        setVisible(true);

        // Dynamische Schriftgrößenanpassung basierend auf der Fenstergröße
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int fontSize = Math.max(12, width / 40); // Skaliere Schriftgröße mit einem Minimum von 12
                Font resizedFont = new Font("SansSerif", Font.PLAIN, fontSize);

                // Wende die skalierte Schriftart auf alle Komponenten an
                userLabel.setFont(resizedFont);
                passLabel.setFont(resizedFont);
                userField.setFont(resizedFont);
                passField.setFont(resizedFont);
                loginButton.setFont(resizedFont);
                registrierButton.setFont(resizedFont);
                passwortVergessenButton.setFont(resizedFont);
            }
        });

        // Event-Handler für Schaltflächen

        // Registrierungs-Schaltfläche - öffnet die GUI zur Profilerstellung
        registrierButton.addActionListener((ActionEvent e) -> {
            new Profil_ErstellenGUI();
            dispose();
        });

        // Login-Schaltfläche - behandelt die Benutzerauthentifizierung
        loginButton.addActionListener((ActionEvent e) -> {
            String benutzer = userField.getText().trim();
            String passwort = new String(passField.getPassword());

            // Rufe das gespeicherte Passwort für den Benutzer ab
            String gespeichertesPasswort = getBenutzerPasswort(benutzer);
            if (gespeichertesPasswort == null) {
                // Benutzer existiert nicht
                JOptionPane.showMessageDialog(this, "Benutzer existiert nicht. Bitte registrieren.");
                userField.setBorder(new LineBorder(Color.RED, 1));
                passField.setBorder(new LineBorder(Color.RED, 1));
            } else {
                // Überprüfe das Passwort
                String eingegebenVerschluesselt = encryptPassword(passwort);
                if (gespeichertesPasswort.equals(eingegebenVerschluesselt)) {
                    // Erfolgreiche Anmeldung
                    Session.login(benutzer);
                    new QuizSelection();
                    dispose();
                } else {
                    // Falsches Passwort
                    JOptionPane.showMessageDialog(null, "Falsches Passwort!");
                    passField.setBorder(new LineBorder(Color.RED, 1));
                }
            }
        });

        // Passwort vergessen-Schaltfläche - öffnet die GUI zum Zurücksetzen des Passworts
        passwortVergessenButton.addActionListener((ActionEvent e) -> {
            new NeuesPasswort();
            dispose();
        });
    }

    /**
     * Stilisiert eine Schaltfläche mit angegebenen Farben und Schriftart.
     * Derzeit nicht verwendet, bietet jedoch Schaltflächen-Stilfunktionalität.
     *
     * @param button Die zu stilende Schaltfläche
     * @param background Hintergrundfarbe
     * @param foreground Textfarbe
     * @param font Anzuwendende Schriftart
     */
    private void styleButton(JButton button, Color background, Color foreground, Font font) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    /**
     * Ruft das gespeicherte Passwort für einen gegebenen Benutzernamen aus der Benutzerdatei ab.
     *
     * @param benutzer Der nachzuschlagende Benutzername
     * @return Das verschlüsselte Passwort, wenn der Benutzer existiert, sonst null
     */
    private String getBenutzerPasswort(String benutzer) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/temp/Quiz/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse Benutzerdaten (Format: Benutzername:Passwort:...)
                String[] parts = line.split(":", 4);
                if (parts.length >= 2 && parts[0].equals(benutzer)) {
                    return parts[1]; // Gib das verschlüsselte Passwort zurück
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Benutzer nicht gefunden
    }

    /**
     * Verschlüsselt ein Passwort mit einem einfachen Bit-Verschiebungsalgorithmus.
     * Verwendet XOR- und Bit-Verschiebeoperationen für die grundlegende Verschlüsselung.
     *
     * @param text Der zu verschlüsselnde Klartext-Passwort
     * @return Das verschlüsselte Passwort als durch Leerzeichen getrennte Zeichenfolge von Zahlen
     */
    private String encryptPassword(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            // Wende Bit-Verschiebung und XOR-Operationen an
            int value = ((c << 5) ^ (c >> 3)) % 256;
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    /**
     * Hauptmethode zum eigenständigen Testen der LoginGUI.
     *
     * @param args Kommandozeilenargumente (nicht verwendet)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
