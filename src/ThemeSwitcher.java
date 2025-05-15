package src;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ThemeSwitcher {

    public static void installThemeShortcut(JFrame rootFrame) {
        JRootPane rootPane = rootFrame.getRootPane();
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "openThemeDialog");
        rootPane.getActionMap().put("openThemeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                showThemeDialog(rootFrame);
            }
        });
    }

    private static void showThemeDialog(JFrame frame) {
        String[] themes = {
                "Flat Light",
                "Flat Dark",
                "Flat Nord",
                "Flat Dracula"
        };

        String selected = (String) JOptionPane.showInputDialog(
                frame,
                "Choose Theme:",
                "Theme Switcher",
                JOptionPane.PLAIN_MESSAGE,
                null,
                themes,
                themes[0]
        );

        if (selected == null) return;

        try {
            switch (selected) {
                case "Flat Light": {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
                }
                case "Flat Dark" :{ UIManager.setLookAndFeel(new FlatDarkLaf()); break; }
                case "Flat Nord" :{ UIManager.setLookAndFeel(new FlatNordIJTheme()); break; }
                case "Flat Dracula" :{ UIManager.setLookAndFeel(new FlatDraculaIJTheme());}
            }

            // Update Look & Feel for all frames
            SwingUtilities.updateComponentTreeUI(frame);
            frame.pack(); // Optional: Größe neu anpassen
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void applyTheme(String selected, JFrame frame) {
        if (selected == null) return;

        try {
            switch (selected) {
                case "Flat Light": UIManager.setLookAndFeel(new FlatLightLaf()); break;
                case "Flat Dark": UIManager.setLookAndFeel(new FlatDarkLaf()); break;
                case "Flat Nord": UIManager.setLookAndFeel(new FlatNordIJTheme()); break;
                case "Flat Dracula": UIManager.setLookAndFeel(new FlatDraculaIJTheme()); break;
            }

            SwingUtilities.updateComponentTreeUI(frame);
            frame.setSize(400, 450);
            frame.pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
