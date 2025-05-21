package src;


import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatNordIJTheme());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginGUI(); // erstes Fenster
        });
    }
}
