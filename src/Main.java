package src;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;

import javax.swing.*;

/**
 * Main class that serves as the entry point for the Quiz application.
 * Sets up the application theme and launches the login GUI.
 */
public class Main {

    /**
     * Main method that initializes the application.
     * Sets the FlatLaf Nord theme and starts the login interface.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set the FlatLaf Nord theme for better UI appearance
        try {
            UIManager.setLookAndFeel(new FlatNordIJTheme());
        } catch (Exception ex) {
            // Print stack trace if theme setting fails
            ex.printStackTrace();
        }

        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create and display the login window as the first interface
            new LoginGUI();
        });
    }
}