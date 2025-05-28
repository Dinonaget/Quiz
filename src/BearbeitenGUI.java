package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BearbeitenGUI extends JFrame {
    private final JComboBox<String> quizSelect;
    private final JComboBox<String> questionSelect;
    private final JTextField questionField;
    private final JTextField[] answerFields = new JTextField[4];
    private final JRadioButton[] radioButtons = new JRadioButton[4];
    private final ButtonGroup radioGroup = new ButtonGroup();
    private final List<Questions> questionsList = new ArrayList<>();
    private String currentQuizFile;

    public BearbeitenGUI() {
        setTitle("Quiz bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dropdowns
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Quiz auswählen:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        quizSelect = new JComboBox<>();
        quizSelect.addActionListener(e -> loadQuestions());
        panel.add(quizSelect, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Frage auswählen:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        questionSelect = new JComboBox<>();
        questionSelect.addActionListener(e -> displayQuestion());
        panel.add(questionSelect, gbc);

        // Fragefeld
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Frage:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        questionField = new JTextField();
        panel.add(questionField, gbc);

        // Antworten + RadioButtons
        for (int i = 0; i < 4; i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            panel.add(new JLabel("Antwort " + (i + 1) + ":"), gbc);

            gbc.gridx = 1;
            answerFields[i] = new JTextField();
            panel.add(answerFields[i], gbc);

            gbc.gridx = 2;
            radioButtons[i] = new JRadioButton();
            radioGroup.add(radioButtons[i]);
            panel.add(radioButtons[i], gbc);
        }

        // Löschen-Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JButton deleteQuestionButton = new JButton("Frage löschen");
        deleteQuestionButton.addActionListener(e -> deleteQuestion());
        panel.add(deleteQuestionButton, gbc);

        gbc.gridx = 1;
        JButton deleteQuizButton = new JButton("Quiz löschen");
        deleteQuizButton.addActionListener(e -> deleteQuiz());
        panel.add(deleteQuizButton, gbc);

        // Speichern-Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        JButton saveButton = new JButton("Änderungen speichern");
        saveButton.addActionListener(e -> saveChanges());
        panel.add(saveButton, gbc);

        // Panel hinzufügen
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);

        loadQuizFiles();
        setVisible(true);
    }

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

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID:")) {
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

            if (question != null) {
                questionsList.add(new Questions(question, answers.toArray(new String[0]), correctIndex));
                questionSelect.addItem(question);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Fragen: " + e.getMessage());
        }
    }

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

    private void deleteQuestion() {
        int selectedIndex = questionSelect.getSelectedIndex();
        if (selectedIndex == -1) return;

        questionsList.remove(selectedIndex);
        questionSelect.removeItemAt(selectedIndex);

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

    private void deleteQuiz() {
        int response = JOptionPane.showConfirmDialog(this, "Sind Sie sicher, dass Sie dieses Quiz löschen möchten?", "Quiz löschen", JOptionPane.YES_NO_OPTION);
        if (response != JOptionPane.YES_OPTION) return;

        File file = new File("C:/temp/Quiz/" + currentQuizFile);
        if (file.delete()) {
            quizSelect.removeItem(currentQuizFile);
            questionsList.clear();
            questionSelect.removeAllItems();
            questionField.setText("");
            for (JTextField field : answerFields) {
                field.setText("");
            }
            radioGroup.clearSelection();
            JOptionPane.showMessageDialog(this, "Quiz erfolgreich gelöscht.");
        } else {
            JOptionPane.showMessageDialog(this, "Fehler beim Löschen des Quiz.");
        }
    }

    private void saveChanges() {
        int selectedIndex = questionSelect.getSelectedIndex();
        if (selectedIndex == -1) return;

        Questions question = questionsList.get(selectedIndex);
        question.setQuestion(questionField.getText());

        String[] answers = new String[4];
        int correctIndex = -1;
        for (int i = 0; i < 4; i++) {
            answers[i] = answerFields[i].getText();
            if (radioButtons[i].isSelected()) {
                correctIndex = i;
            }
        }

        if (correctIndex == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie die richtige Antwort aus.");
            return;
        }

        question.setAnswers(answers);
        question.setCorrectIndex(correctIndex);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/temp/Quiz/" + currentQuizFile))) {
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
