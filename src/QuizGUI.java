package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Eine GUI-Klasse für ein Quiz.
 * Diese Klasse bietet eine grafische Benutzeroberfläche für ein Quiz,
 * das Fragen aus einer Datei lädt und dem Benutzer ermöglicht, diese zu beantworten.
 */

public class QuizGUI extends JFrame {
    private List<Questions> questionsList = new ArrayList<>();
    private List<Questions> skippedQuestions = new LinkedList<>();
    private int currentIndex = 0;
    private int score = 0;
    private boolean reviewingSkipped = false;
    private int qAmount;
    private int qNumber = 0; // Zähler für Fragen in einem Quiz

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton, skipButton;

    /**
     * Erstellt ein neues QuizGUI und lädt Fragen aus einer Datei.
     * @param filename Der Name der Datei, aus der die Fragen geladen werden sollen.
     */

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

    /**
     * Richtet die grafische Benutzeroberfläche für das Quiz ein.
     */

    private void setupGUI() {
        setTitle("Quiz App");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Oben: Frage
        questionLabel = new JLabel("Frage", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(questionLabel, BorderLayout.NORTH);

        // Mitte: Antworten
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        options = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("SansSerif", Font.PLAIN, 16));
            options[i].setOpaque(false);
            group.add(options[i]);
            optionsPanel.add(options[i]);
        }

        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Unten: Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        nextButton = new JButton("Weiter");
        skipButton = new JButton("Überspringen");

        nextButton.setPreferredSize(new Dimension(150, 40));
        skipButton.setPreferredSize(new Dimension(150, 40));
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        skipButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        nextButton.addActionListener(e -> checkAndNext());
        skipButton.addActionListener(e -> skipQuestion());

        bottomPanel.add(nextButton);
        bottomPanel.add(skipButton);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        bottomPanel.getRootPane().setDefaultButton(nextButton);
    }

    /**
     * Zeigt die aktuelle Frage an.
     */

    private void showQuestion() {

        if (currentIndex >= questionsList.size()) {

            // Zeige übersprungene Fragen
            if (!reviewingSkipped && !skippedQuestions.isEmpty()) {
                // Wenn Fragen übersprungen wurden, merke dir die ursprüngliche Anzahl der Fragen
                if (qNumber == 0)
                {qNumber = questionsList.size();}

                questionsList = new ArrayList<>(skippedQuestions);
                skippedQuestions.clear();
                currentIndex = 0;
                reviewingSkipped = true;
                JOptionPane.showMessageDialog(this, "⏭ Jetzt folgen die übersprungenen Fragen.");
                showQuestion();
                return;
            }
            showResult();

        }
        // Zeige reguläre Fragen
        Questions q = questionsList.get(currentIndex);
        questionLabel.setText("Frage " + (currentIndex + 1) + ": " + q.getQuestion());

        String[] answers = q.getAnswers();
        for (int i = 0; i < answers.length; i++) {
            options[i].setText(answers[i]);
            options[i].setSelected(false);

        }
        // Wenn keine Fragen übersprungen wurden, verwende die aktuelle Anzahl der Fragen
        if(!reviewingSkipped) {
            qNumber = questionsList.size();
        }
    }

    /**
     * Überprüft die ausgewählte Antwort und geht zur nächsten Frage.
     */

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

    /**
     * Überspringt die aktuelle Frage.
     */

    private void skipQuestion() {
        skippedQuestions.add(questionsList.get(currentIndex));
        currentIndex++;
        showQuestion();
    }

    /**
     * Zeigt das Ergebnis des Quiz an.
     */

    private void showResult() {
        JOptionPane.showMessageDialog(this,
                "✅ Du hast " + score + " von " + qNumber + " richtig beantwortet.");
        dispose();
    }

    /**
     * Lädt Fragen aus einer Datei.
     * @param filename Der Name der Datei, aus der die Fragen geladen werden sollen.
     */

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
