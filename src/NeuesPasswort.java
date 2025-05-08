package src;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NeuesPasswort extends JFrame {

    public NeuesPasswort() {
        setTitle("Passwort ändern");
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

        // Passwort-Feld 1
        JLabel pass1Label = new JLabel("Neues Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(pass1Label, gbc);

        JPasswordField pass1Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(pass1Field, gbc);

        // Passwort-Feld 2
        JLabel pass2Label = new JLabel("Passwort bestätigen:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(pass2Label, gbc);

        JPasswordField pass2Field = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(pass2Field, gbc);

        // Bestätigungs-Button
        JButton confirmButton = new JButton("Bestätigen");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(confirmButton, gbc);

        setVisible(true);

        // Aktion bei Button-Klick
        confirmButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String raw1Password = new String(pass1Field.getPassword());
            String raw2Password = new String(pass2Field.getPassword());

            if (username.isEmpty() || raw1Password.isEmpty() || raw2Password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alle Felder müssen ausgefüllt sein!");
                return;
            }

            if (!raw1Password.equals(raw2Password)) {
                JOptionPane.showMessageDialog(this, "Passwörter müssen übereinstimmen!");
                return;
            }

            String encryptedPassword = encryption(raw1Password);
            try {
                boolean updated = updateUserPassword("users.txt", username, encryptedPassword);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Passwort erfolgreich geändert.");
                    new QuizSelection(); // Zur nächsten GUI wechseln
                    dispose(); // Aktuelles Fenster schließen
                } // Sonst wurde bereits eine Fehlermeldung angezeigt
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern!");
            }
        });
    }

    /**
     * Ändert das Passwort eines bestehenden Benutzers in der Datei.
     * Gibt true zurück, wenn Benutzer gefunden und geändert wurde.
     * Zeigt Fehlermeldung, wenn Benutzer nicht existiert.
     */
    public static boolean updateUserPassword(String filename, String username, String encryptedPassword) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "Benutzerdatenbank nicht gefunden!", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<String> lines = Files.readAllLines(Paths.get(filename));
        boolean userFound = false;
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            JSONObject obj = new JSONObject(line);
            if (obj.getString("username").equals(username)) {
                obj.put("password", encryptedPassword); // Passwort aktualisieren
                userFound = true;
            }
            updatedLines.add(obj.toString());
        }

        if (!userFound) {
            JOptionPane.showMessageDialog(null, "Benutzer \"" + username + "\" existiert nicht!", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Files.write(Paths.get(filename), updatedLines);
        return true;
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
