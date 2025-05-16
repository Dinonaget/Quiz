package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuizSelection extends JFrame {
    private List<JButton> buttons = new ArrayList<>();

    public QuizSelection() {
        super("Quiz Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        int fixedWidth = 500;
        int maxWidth = 1000;
        int maxHeight = 800;

        // Menüleiste erstellen
        JMenuBar menuBar = new JMenuBar();
        JMenu einstellungenMenu = new JMenu("Einstellungen");

        JMenuItem themeItem = new JMenuItem("Theme wechseln");
        themeItem.addActionListener(e -> ThemeSwitcher.showThemeDialog(this));
        einstellungenMenu.add(themeItem);

        JMenuItem infoItem = new JMenuItem("Info");
        // noch nicht implementiert
        einstellungenMenu.add(infoItem);

        menuBar.add(einstellungenMenu);
        setJMenuBar(menuBar);

        // Hauptpanel mit Quizbuttons
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Quiz Selection");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        File quizDir = new File("C:/temp/Quiz");
        File[] files = quizDir.listFiles((dir, name) -> name.endsWith(".txt") && !name.equalsIgnoreCase("users.txt"));

        int row = 1;
        if (files != null) {
            for (File file : files) {
                String name = file.getName().replace(".txt", "");
                JButton quizButton = new JButton(name);
                quizButton.setToolTipText("Start Quiz: " + name);
                quizButton.addActionListener(e -> new QuizGUI(file.getName()));
                gbc.gridy = row++;
                panel.add(quizButton, gbc);
                buttons.add(quizButton);
            }
        }

        JButton erstellenButton = new JButton("Neues Quiz erstellen");
        erstellenButton.addActionListener(e -> new QuizErstellenGUI());
        gbc.gridy = row++;
        panel.add(erstellenButton, gbc);
        buttons.add(erstellenButton);

        add(panel);
        panel.setPreferredSize(new Dimension(fixedWidth, panel.getPreferredSize().height));
        pack();
        setLocationRelativeTo(null);
        setMaximumSize(new Dimension(maxWidth, maxHeight));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                aktualisiereFonts(label, buttons);
            }
        });

        setVisible(true);
    }

    private void aktualisiereFonts(JLabel label, List<JButton> buttons) {
        int breite = getWidth();
        float faktor = breite / 500.0f;

        int labelFontSize = Math.round(20 * faktor);
        int buttonFontSize = Math.round(16 * faktor);

        label.setFont(new Font("Arial", Font.BOLD, labelFontSize));
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.PLAIN, buttonFontSize));
        }
    }
}
