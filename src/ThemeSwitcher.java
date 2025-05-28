package src;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.intellijthemes.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Eine Klasse zum Umschalten von Themes in einer Swing-Anwendung.
 * Diese Klasse bietet eine grafische Benutzeroberfläche zur Auswahl und Anwendung verschiedener Themes.
 */

public class ThemeSwitcher {

    // Map zur Anzeige und Zuordnung der Themes
    private static final Map<String, LookAndFeel> availableThemes = new LinkedHashMap<>();

    static {
        // Light Themes
        availableThemes.put("Light/Flat Light", new FlatLightLaf());
        availableThemes.put("Light/Arc", new FlatArcIJTheme());
        availableThemes.put("Light/Arc - Orange", new FlatArcOrangeIJTheme());
        availableThemes.put("Light/Cyan light", new FlatCyanLightIJTheme());
        availableThemes.put("Light/Light Flat", new FlatLightFlatIJTheme());
        availableThemes.put("Light/Solarized Light", new FlatSolarizedLightIJTheme());

        // Dark Themes
        availableThemes.put("Dark/Vueston", new FlatVuesionIJTheme());
        availableThemes.put("Dark/Flat Dark", new FlatDarkLaf());
        availableThemes.put("Dark/Arc Dark", new FlatArcDarkIJTheme());
        availableThemes.put("Dark/Arc Dark - Orange", new FlatArcDarkOrangeIJTheme());
        availableThemes.put("Dark/Carbon", new FlatCarbonIJTheme());
        availableThemes.put("Dark/Cobalt 2", new FlatCobalt2IJTheme());
        availableThemes.put("Dark/Dark Flat", new FlatDarkFlatIJTheme());
        availableThemes.put("Dark/Dark purple", new FlatDarkPurpleIJTheme());
        availableThemes.put("Dark/Dracula", new FlatDraculaIJTheme());
        availableThemes.put("Dark/Gradianto Dark Fuchsia", new FlatGradiantoDarkFuchsiaIJTheme());
        availableThemes.put("Dark/Gradianto Deep Ocean", new FlatGradiantoDeepOceanIJTheme());
        availableThemes.put("Dark/Gradianto Midnight Blue", new FlatGradiantoMidnightBlueIJTheme());
        availableThemes.put("Dark/High Contrast", new FlatHighContrastIJTheme());
        availableThemes.put("Dark/Material Design Dark", new FlatMaterialDesignDarkIJTheme());
        availableThemes.put("Dark/Monocal", new FlatMonocaiIJTheme());
        availableThemes.put("Dark/Monokai Pro", new FlatMonokaiProIJTheme());
        availableThemes.put("Dark/Nord", new FlatNordIJTheme());
        availableThemes.put("Dark/One Dark", new FlatOneDarkIJTheme());
        availableThemes.put("Dark/Solarized Dark", new FlatSolarizedDarkIJTheme());
        availableThemes.put("Dark/Spacegray", new FlatSpacegrayIJTheme());
        availableThemes.put("Dark/Xcode-Dark", new FlatXcodeDarkIJTheme());
    }

    /**
     * Zeigt einen Dialog zur Auswahl eines Themes an.
     * @param frame Das JFrame, in dem der Dialog angezeigt wird.
     */

    public static void showThemeDialog(JFrame frame) {
        String[] themeNames = availableThemes.keySet().toArray(new String[0]);

        String selected = (String) JOptionPane.showInputDialog(
                frame,
                "Choose Theme:",
                "Theme Switcher",
                JOptionPane.PLAIN_MESSAGE,
                null,
                themeNames,
                themeNames[0]
        );

        if (selected != null) {
            applyTheme(selected, frame);
        }
    }

    /**
     * Wendet das ausgewählte Theme auf das JFrame an.
     * @param themeName Der Name des Themes, das angewendet werden soll.
     * @param frame Das JFrame, auf das das Theme angewendet werden soll.
     */

    public static void applyTheme(String themeName, JFrame frame) {
        LookAndFeel laf = availableThemes.get(themeName);
        if (laf == null) return;

        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(frame);
            frame.invalidate();
            frame.validate();
            frame.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
