package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;

public class QuizSelection extends JFrame {
    public QuizSelection() {
        super("Quiz Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Optional: kein Resize erlauben

        int fixedWidth = 400;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Quiz Selection");
        label.setFont(new Font("Arial", Font.BOLD, 15));
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
            }
        }

        JButton erstellenButton = new JButton("Neues Quiz erstellen");
        erstellenButton.addActionListener(e -> new QuizErstellenGUI());
        gbc.gridy = row++;
        panel.add(erstellenButton, gbc);

        // ThemeSwitcher Dropdown
        JComboBox<String> themeSelector = new JComboBox<>(new String[]{"Flat Light", "Flat Dark", "Flat Nord", "Flat Dracula"});
        themeSelector.addActionListener(e -> ThemeSwitcher.applyTheme((String) themeSelector.getSelectedItem(), this));
        gbc.gridy = row++;
        panel.add(themeSelector, gbc);

        add(panel);
        panel.setPreferredSize(new Dimension(fixedWidth, panel.getPreferredSize().height));
        pack(); // passt nur die Höhe an
        setLocationRelativeTo(null); // erst nach pack() aufrufen

        setVisible(true);
    }
}
