package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * GUI for conducting a quiz with multiple-choice questions.
 * Supports skipping questions, reviewing skipped questions, and displays results.
 */
public class QuizGUI extends JFrame {
    private List<Questions> questionsList = new ArrayList<>();
    private List<Questions> skippedQuestions = new LinkedList<>();
    private int currentIndex = 0;
    private int score = 0;
    private boolean reviewingSkipped = false;

    /**
     * Count of questions in a quiz
     */
    private int qNumber = 0;

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton, skipButton;
    private JPanel optionsPanel;
    private Timer timer;

    /**
     * Constructs a new QuizGUI with questions loaded from the specified file.
     *
     * @param filename The name of the file containing quiz questions
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
     * Sets up the graphical user interface components.
     */
    private void setupGUI() {
        setTitle("Quiz App");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        /**
         * Top section: Question display
         */
        questionLabel = new JLabel("Frage", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(questionLabel, BorderLayout.NORTH);

        /**
         * Middle section: Answer options
         */
        optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        options = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("SansSerif", Font.PLAIN, 16));
            options[i].setOpaque(true);
            group.add(options[i]);
            optionsPanel.add(options[i]);
        }

        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        /**
         * Bottom section: Control buttons
         */
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
     * Displays the current question and its answer options.
     * If all questions are completed, shows skipped questions or final results.
     */
    private void showQuestion() {

        if (currentIndex >= questionsList.size()) {

            /**
             * Show skipped questions if available
             */
            if (!reviewingSkipped && !skippedQuestions.isEmpty()) {
                /**
                 * If question is skipped, remember initial "questionsList.size()"
                 */
                if (qNumber == 0)
                {qNumber = questionsList.size();}

                questionsList = new ArrayList<>(skippedQuestions);
                skippedQuestions.clear();
                currentIndex = 0;
                reviewingSkipped = true;
                JOptionPane.showMessageDialog(this, "Jetzt folgen die übersprungenen Fragen.");
                showQuestion();
                return;
            }
            showResult();

        }

        if (currentIndex >= questionsList.size()) {
            return;
        }
        /**
         * Show regular questions
         */
        Questions q = questionsList.get(currentIndex);
        questionLabel.setText("Frage " + (currentIndex + 1) + ": " + q.getQuestion());

        String[] answers = q.getAnswers();
        for (int i = 0; i < answers.length; i++) {
            options[i].setText(answers[i]);
            options[i].setSelected(false);
            options[i].setBackground(null);
        }

        /**
         * If there are no skipped questions, use current "questionsList.size()"
         */
        if(!reviewingSkipped) {
            qNumber = questionsList.size();
        }
    }

    /**
     * Checks the selected answer and proceeds to the next question.
     * Displays correct/incorrect feedback with color coding.
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

        Color correctColor;
        Color incorrectColor;

        /**
         * Choose theme-specific colors
         */
        if (UIManager.getLookAndFeel().getName().toLowerCase().contains("light")) {
            /**
             * For dark themes - richer colors, lighter text
             */
            correctColor = new Color(0xA9E5B7);     // light green
            incorrectColor = new Color(0xFFB3B3);   // light red

        } else {
            /**
             * For light themes - lighter colors, darker text
             */
            correctColor = new Color(0x339966);     // dark green
            incorrectColor = new Color(0xCC3333);   // dark red


        }


        Questions q = questionsList.get(currentIndex);
        if (selected == q.getCorrectIndex()) {
            score++;
            options[selected].setBackground(correctColor);
        } else {
            options[selected].setBackground(incorrectColor);
            options[q.getCorrectIndex()].setBackground(correctColor);
        }

        nextButton.setEnabled(false);
        skipButton.setEnabled(false);

        /**
         * Timer for 3 seconds (3000 milliseconds)
         */
        timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentIndex++;
                showQuestion();
                nextButton.setEnabled(true);
                skipButton.setEnabled(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Skips the current question and adds it to the skipped questions list.
     */
    private void skipQuestion() {
        skippedQuestions.add(questionsList.get(currentIndex));
        currentIndex++;
        showQuestion();
    }

    /**
     * Displays the final quiz results showing score and total questions.
     */
    private void showResult() {
        JOptionPane.showMessageDialog(this,
                "Du hast " + score + " von " + qNumber + " richtig beantwortet.");
        dispose();
    }

    /**
     * Loads questions from the specified file.
     * File format expected: ID, question, 4 answers, correct answer index.
     *
     * @param filename The name of the file to load questions from
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
            JOptionPane.showMessageDialog(this, "Fehler beim Lesen der Datei:\n" + fullPath + "\n" + e.getMessage());
        }
    }
}