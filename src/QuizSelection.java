package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;

public class QuizSelection extends JFrame {

    public QuizSelection() {
        super("Quiz Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel label = new JLabel("Quiz Selection");
        label.setFont(new Font("Arial", Font.BOLD, 15));
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        label.setBorder(border);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        // Dropdown (ComboBox) zum Thema-Auswählen
        String[] themes = {"Flat Light", "Flat Dark", "Flat Nord", "Flat Dracula"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);
        themeComboBox.setSelectedItem("Flat Nord");  // Default Theme

        gbc.gridy = 1;
        panel.add(themeComboBox, gbc);

        themeComboBox.addActionListener(e -> {
            String selectedTheme = (String) themeComboBox.getSelectedItem();
            ThemeSwitcher.applyTheme(selectedTheme, this);
        });

        File quizDir = new File("C:/temp/Quiz");
        File[] files = quizDir.listFiles((dir, name) -> name.endsWith(".txt") && !name.equalsIgnoreCase("users.txt"));

        int row = 2;
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String buttonLabel = fileName.substring(0, fileName.length() - 4);

                JButton quizButton = new JButton(buttonLabel);
                quizButton.setToolTipText("Start Quiz: " + buttonLabel);

                quizButton.addActionListener(ev -> new QuizGUI(file.getName()));

                gbc.gridx = 0;
                gbc.gridy = row++;
                panel.add(quizButton, gbc);
            }
        }

        JButton erstellenButton = new JButton("Neues Quiz erstellen");
        erstellenButton.setToolTipText("Öffnet das Fenster zum Erstellen eines neuen Quiz");
        erstellenButton.addActionListener(ev -> new QuizErstellenGUI());

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(erstellenButton, gbc);

        add(panel);
        setResizable(false);
        setVisible(true);
    }
}
