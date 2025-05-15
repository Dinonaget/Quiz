package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class QuizGUI extends JFrame {
    private ArrayList<Questions> questionsList = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;

    public QuizGUI(String filename) {
        loadQuestionsFromFile(filename);

        if (questionsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Fragen gefunden!");
            dispose();
            return;
        }

        setupGUI();
        showQuestion();
    }

    private void setupGUI() {
        setTitle("Quiz App");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        questionLabel = new JLabel("Frage", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        options = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            optionsPanel.add(options[i]);
        }

        add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Weiter");
        nextButton.addActionListener(e -> checkAndNext());
        add(nextButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showQuestion() {
        if (currentIndex >= questionsList.size()) {
            showResult();
            return;
        }

        Questions q = questionsList.get(currentIndex);
        questionLabel.setText("Frage " + (currentIndex + 1) + ": " + q.getQuestion());

        String[] answers = q.getAnswers();
        for (int i = 0; i < answers.length; i++) {
            options[i].setText(answers[i]);
            options[i].setSelected(false);
        }
    }

    private void checkAndNext() {
        int selected = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                selected = i;
                break;
            }
        }

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Bitte eine Antwort auswählen.");
            return;
        }

        Questions q = questionsList.get(currentIndex);
        if (selected == q.getCorrectIndex()) {
            score++;
        }

        currentIndex++;
        showQuestion();
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this,
                "✅ Du hast " + score + " von " + questionsList.size() + " richtig!");
        dispose();
    }

    private void loadQuestionsFromFile(String filename) {
        String fullPath = "C:/temp/Quiz/" + filename;
        try {
            java.util.List<String> lines = Files.readAllLines(Paths.get(fullPath));
            for (int i = 0; i + 5 < lines.size(); i += 6) {
                String question = lines.get(i);
                String[] answers = new String[4];
                for (int j = 0; j < 4; j++) {
                    answers[j] = lines.get(i + 1 + j);
                }
                int correct;
                try {
                    correct = Integer.parseInt(lines.get(i + 5));
                    if (correct < 0 || correct > 3) continue;
                } catch (NumberFormatException e) {
                    continue;
                }

                Questions q = new Questions(question, answers, correct);
                questionsList.add(q);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Fehler beim Lesen der Datei:\n" + fullPath + "\n" + e.getMessage());
        }
    }
}
