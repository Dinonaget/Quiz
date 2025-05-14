package src;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Profil_ErstellenGUI extends JFrame {

    public Profil_ErstellenGUI() {
        setTitle("Profil erstellen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Benutzername-Feld
        JLabel userLabel = new JLabel("Benutzername:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userField, gbc);

        // Passwort-Feld
        JLabel pass1Label = new JLabel("Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(pass1Label, gbc);

        JPasswordField pass1Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(pass1Field, gbc);

        // Passwort-Feld
        JLabel pass2Label = new JLabel("Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(pass2Label, gbc);

        JPasswordField pass2Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(pass2Field, gbc);

        // Button zum Bestätigen
        JButton confirmButton = new JButton("Bestätigen");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(confirmButton, gbc);

        setVisible(true);

        // Aktion bei Button-Klick
        confirmButton.addActionListener(e -> {
            String username = userField.getText();
            String raw1Password = new String(pass1Field.getPassword());
            String raw2Password = new String(pass2Field.getPassword());
            if (raw1Password.equals(raw2Password)) {
                String encryptedPassword = encryption(raw1Password);
                try {
                    writeUser("users.txt", username, encryptedPassword);
                } catch (IOException ex) {
                    ex.printStackTrace(); // Fehlerausgabe (keine Dialogbox)
                }

                new QuizSelection(); // Weiter zur nächsten GUI
                dispose(); // Aktuelles Fenster schließen
            }else {
                JOptionPane.showMessageDialog(this, "Passwörter müssen übereinstimmen!");
            }

        });
    }

    /**
     * Speichert Username und verschlüsseltes Passwort als Textzeile in einer Datei.
     */
    public static void writeUser(String filename, String username, String encryptedPassword) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/temp/Quiz/users.txt", true))) {
            writer.write(username + ":" + encryptedPassword);
            writer.newLine();
        }
    }

    /**
     * Verschlüsselt einen String durch Bitoperationen und gibt Zahlenfolge zurück.
     */
    public static String encryption(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int value = (int) text.charAt(i);
            value = ((value << 5) ^ (value >> 3)) % 256;
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Profil_ErstellenGUI::new);
    }
}
