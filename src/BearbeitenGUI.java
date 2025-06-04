package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI class for editing quiz questions and managing quiz files.
 * Provides functionality to load, edit, add, delete questions and quizzes.
 */
public class BearbeitenGUI extends JFrame {
    /** Base font used for all UI components */
    private Font baseFont = new Font("SansSerif", Font.PLAIN, 14);

    /** Dropdown for selecting quiz files */
    private final JComboBox<String> quizSelect;

    /** Dropdown for selecting individual questions within a quiz */
    private final JComboBox<String> questionSelect;

    /** Text field for editing the question text */
    private final JTextField questionField;

    /** Array of text fields for the four answer options */
    private final JTextField[] answerFields = new JTextField[4];

    /** Array of radio buttons to select the correct answer */
    private final JRadioButton[] radioButtons = new JRadioButton[4];

    /** Button group to ensure only one radio button can be selected */
    private final ButtonGroup radioGroup = new ButtonGroup();

    /** List storing all questions from the currently selected quiz */
    private final List<Questions> questionsList = new ArrayList<>();

    /** Name of the currently loaded quiz file */
    private String currentQuizFile;

    /** Reference to the parent QuizSelection GUI */
    private QuizSelection parent;

    /** Labels for dynamic font size adjustment */
    private JLabel quizSelectLabel;
    private JLabel questionSelectLabel;
    private JLabel questionLabel;
    private JLabel[] answerLabels = new JLabel[4];
    private JButton deleteQuestionButton;
    private JButton deleteQuizButton;
    private JButton addQuestionButton;
    private JButton saveButton;

    /**
     * Constructor that initializes the GUI components and sets up the layout.
     *
     * @param parent The parent QuizSelection window to refresh after saving or deleting.
     */
    public BearbeitenGUI(QuizSelection parent) {
        this.parent = parent;
        setTitle("Quiz bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);

        // Create main panel with GridBagLayout for flexible component arrangement
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Quiz selection dropdown
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        quizSelectLabel = new JLabel("Quiz auswählen:");
        quizSelectLabel.setFont(baseFont);
        panel.add(quizSelectLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        quizSelect = new JComboBox<>();
        quizSelect.setFont(baseFont);
        quizSelect.addActionListener(e -> loadQuestions());
        panel.add(quizSelect, gbc);

        // Question selection dropdown
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        questionSelectLabel = new JLabel("Frage auswählen:");
        questionSelectLabel.setFont(baseFont);
        panel.add(questionSelectLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        questionSelect = new JComboBox<>();
        questionSelect.setFont(baseFont);
        questionSelect.addActionListener(e -> displayQuestion());
        panel.add(questionSelect, gbc);

        // Question text field
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        questionLabel = new JLabel("Frage:");
        questionLabel.setFont(baseFont);
        panel.add(questionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        questionField = new JTextField();
        questionField.setFont(baseFont);
        panel.add(questionField, gbc);

        // Create answer fields and corresponding radio buttons
        for (int i = 0; i < 4; i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            answerLabels[i] = new JLabel("Antwort " + (i + 1) + ":");
            answerLabels[i].setFont(baseFont);
            panel.add(answerLabels[i], gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            answerFields[i] = new JTextField();
            answerFields[i].setFont(baseFont);
            panel.add(answerFields[i], gbc);

            gbc.gridx = 3;
            radioButtons[i] = new JRadioButton();
            radioButtons[i].setFont(baseFont);
            radioGroup.add(radioButtons[i]);
            panel.add(radioButtons[i], gbc);
        }

        // Create button panel with action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        deleteQuestionButton = new JButton("Frage löschen");
        deleteQuestionButton.setFont(baseFont);
        deleteQuestionButton.addActionListener(e -> deleteQuestion());
        buttonPanel.add(deleteQuestionButton);

        deleteQuizButton = new JButton("Quiz löschen");
        deleteQuizButton.setFont(baseFont);
        deleteQuizButton.addActionListener(e -> deleteQuiz());
        buttonPanel.add(deleteQuizButton);

        addQuestionButton = new JButton("Frage hinzufügen");
        addQuestionButton.setFont(baseFont);
        addQuestionButton.addActionListener(e -> addQuestion());
        buttonPanel.add(addQuestionButton);

        saveButton = new JButton("Änderungen speichern");
        saveButton.setFont(baseFont);
        saveButton.addActionListener(e -> saveChanges());
        buttonPanel.add(saveButton);
        getRootPane().setDefaultButton(saveButton);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

        // Add scroll pane for better usability with large content
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);

        // Add component listener for dynamic font scaling based on window size
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int fontSize = Math.max(12, width / 40); // Scale font size based on window width
                Font resizedFont = new Font("SansSerif", Font.PLAIN, fontSize);

                // Update font for all labels
                quizSelectLabel.setFont(resizedFont);
                questionSelectLabel.setFont(resizedFont);
                questionLabel.setFont(resizedFont);
                for (JLabel label : answerLabels) {
                    label.setFont(resizedFont);
                }

                // Update font for input components
                quizSelect.setFont(resizedFont);
                questionSelect.setFont(resizedFont);
                questionField.setFont(resizedFont);
                for (JTextField field : answerFields) {
                    field.setFont(resizedFont);
                }

                // Update font for radio buttons
                for (JRadioButton radio : radioButtons) {
                    radio.setFont(resizedFont);
                }

                // Update font for action buttons
                deleteQuestionButton.setFont(resizedFont);
                deleteQuizButton.setFont(resizedFont);
                addQuestionButton.setFont(resizedFont);
                saveButton.setFont(resizedFont);
            }
        });

        loadQuizFiles();
        setVisible(true);
    }

    /**
     * Loads all quiz files from the quiz directory and populates the quiz selection dropdown.
     */
    private void loadQuizFiles() {
        File quizDir = new File("C:/temp/Quiz");
        File[] files = quizDir.listFiles(new java.io.FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });

        if (files != null) {
            for (File file : files) {
                quizSelect.addItem(file.getName());
            }
        }
    }

    /**
     * Loads all questions from the selected quiz file and populates the question dropdown.
     */
    private void loadQuestions() {
        String selectedQuiz = (String) quizSelect.getSelectedItem();
        if (selectedQuiz == null) return;

        currentQuizFile = selectedQuiz;
        questionsList.clear();
        questionSelect.removeAllItems();

        try (BufferedReader reader = new BufferedReader(new FileReader("C:/temp/Quiz/" + selectedQuiz))) {
            String line;
            String question = null;
            List<String> answers = new ArrayList<>();
            int correctIndex = -1;

            // Parse the quiz file line by line
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID:")) {
                    // Save the previous question if it exists
                    if (question != null) {
                        questionsList.add(new Questions(question, answers.toArray(new String[0]), correctIndex));
                        questionSelect.addItem(question);
                        question = null;
                        answers.clear();
                    }
                } else if (question == null) {
                    question = line;
                } else if (answers.size() < 4) {
                    answers.add(line);
                } else {
                    correctIndex = Integer.parseInt(line);
                }
            }

            // Add the last question if it exists
            if (question != null) {
                questionsList.add(new Questions(question, answers.toArray(new String[0]), correctIndex));
                questionSelect.addItem(question);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Fragen: " + e.getMessage());
        }
    }

    /**
     * Displays the selected question and its answers in the input fields.
     */
    private void displayQuestion() {
        int selectedIndex = questionSelect.getSelectedIndex();
        if (selectedIndex == -1) return;

        Questions question = questionsList.get(selectedIndex);
        questionField.setText(question.getQuestion());

        String[] answers = question.getAnswers();
        for (int i = 0; i < answers.length; i++) {
            answerFields[i].setText(answers[i]);
            radioButtons[i].setSelected(i == question.getCorrectIndex());
        }
    }

    /**
     * Deletes the currently selected question from the quiz.
     */
    private void deleteQuestion() {
        int selectedIndex = questionSelect.getSelectedIndex();
        if (selectedIndex == -1) return;

        questionsList.remove(selectedIndex);
        questionSelect.removeItemAt(selectedIndex);

        // Clear fields if no questions remain, otherwise display the first question
        if (questionsList.isEmpty()) {
            questionField.setText("");
            for (JTextField field : answerFields) {
                field.setText("");
            }
            radioGroup.clearSelection();
        } else {
            questionSelect.setSelectedIndex(0);
            displayQuestion();
        }
    }

    /**
     * Deletes the entire currently selected quiz file after user confirmation.
     */
    private void deleteQuiz() {
        int response = JOptionPane.showConfirmDialog(this, "Sind Sie sicher, dass Sie dieses Quiz löschen möchten?", "Quiz löschen", JOptionPane.YES_NO_OPTION);
        if (response != JOptionPane.YES_OPTION) return;

        File file = new File("C:/temp/Quiz/" + currentQuizFile);
        if (file.delete()) {
            // Remove quiz from dropdown and clear all fields
            quizSelect.removeItem(currentQuizFile);
            questionsList.clear();
            questionSelect.removeAllItems();
            questionField.setText("");
            for (JTextField field : answerFields) {
                field.setText("");
            }
            radioGroup.clearSelection();
            JOptionPane.showMessageDialog(this, "Quiz erfolgreich gelöscht.");

            // Close the current BearbeitenGUI
            this.dispose();

            // Refresh the QuizSelection GUI
            if (parent != null) {
                parent.refreshContent();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Fehler beim Löschen des Quiz.");
        }
    }

    /**
     * Adds a new question to the current quiz based on the input field values.
     */
    private void addQuestion() {
        String question = questionField.getText().trim();
        if (question.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fragetext darf nicht leer sein.");
            return;
        }

        String[] answers = new String[4];
        int correctIndex = -1;

        // Validate all answer fields and find the selected correct answer
        for (int i = 0; i < 4; i++) {
            answers[i] = answerFields[i].getText().trim();
            if (answers[i].isEmpty()) {
                JOptionPane.showMessageDialog(this, "Antwortfeld " + (i + 1) + " ist leer.");
                return;
            }
            if (radioButtons[i].isSelected()) {
                correctIndex = i;
            }
        }

        if (correctIndex == -1) {
            JOptionPane.showMessageDialog(this, "Bitte richtige Antwort auswählen.");
            return;
        }

        // Create new question and add it to the list
        Questions newQuestion = new Questions(question, answers, correctIndex);
        questionsList.add(newQuestion);
        questionSelect.addItem(question);
        questionSelect.setSelectedItem(question);

        // Clear input fields after adding the question
        questionField.setText("");
        for (JTextField field : answerFields) {
            field.setText("");
        }
        radioGroup.clearSelection();
    }

    /**
     * Saves all changes made to the current quiz back to the file.
     */
    private void saveChanges() {
        if (currentQuizFile == null) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/temp/Quiz/" + currentQuizFile))) {
            // Write all questions in the proper format
            for (Questions q : questionsList) {
                writer.write("ID:" + q.getId());
                writer.newLine();
                writer.write(q.getQuestion());
                writer.newLine();
                for (String answer : q.getAnswers()) {
                    writer.write(answer);
                    writer.newLine();
                }
                writer.write(Integer.toString(q.getCorrectIndex()));
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Änderungen erfolgreich gespeichert.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Änderungen: " + e.getMessage());
        }
    }
}
