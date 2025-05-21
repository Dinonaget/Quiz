package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuizGUI extends JFrame {
    private List<Questions> questionsList = new ArrayList<>();
    private List<Questions> skippedQuestions = new LinkedList<>();
    private int currentIndex = 0;
    private int score = 0;
    private boolean reviewingSkipped = false;

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton, skipButton;

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
        setSize(500, 350);
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

        JPanel bottomPanel = new JPanel(new FlowLayout());
        nextButton = new JButton("Weiter");
        skipButton = new JButton("Überspringen");

        nextButton.addActionListener(e -> checkAndNext());
        skipButton.addActionListener(e -> skipQuestion());
        nextButton.addActionListener(e -> checkAndNext());// Event-Listener für den Button
        getRootPane().setDefaultButton(nextButton);
        add(nextButton, BorderLayout.SOUTH); // Button zum Fenster hinzufügen

        bottomPanel.add(nextButton);
        bottomPanel.add(skipButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showQuestion() {
        if (currentIndex >= questionsList.size()) {
            if (!reviewingSkipped && !skippedQuestions.isEmpty()) {
                // Starte zweite Runde mit übersprungenen Fragen
                questionsList = new ArrayList<>(skippedQuestions);
                skippedQuestions.clear();
                currentIndex = 0;
                reviewingSkipped = true;
                JOptionPane.showMessageDialog(this, "⏭ Jetzt folgen die übersprungenen Fragen.");
                showQuestion();
                return;
            }

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

    private void skipQuestion() {
        skippedQuestions.add(questionsList.get(currentIndex));
        currentIndex++;
        showQuestion();
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this,
                "✅ Du hast " + score + " von " + (score + skippedQuestions.size()) + " richtig beantwortet.");
        dispose();
    }

    private void loadQuestionsFromFile(String filename) {
        String fullPath = "C:/temp/Quiz/" + filename;
        try {
            java.util.List<String> lines = Files.readAllLines(Paths.get(fullPath));
            for (int i = 0; i + 6 < lines.size(); i += 7) {
                String idLine = lines.get(i);
                if (!idLine.startsWith("ID:")) continue;

                String question = lines.get(i + 1);
                String[] answers = new String[4];
                for (int j = 0; j < 4; j++) {
                    answers[j] = lines.get(i + 2 + j);
                }

                int correct;
                try {
                    correct = Integer.parseInt(lines.get(i + 6));
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
