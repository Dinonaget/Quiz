package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * GUI for creating a new quiz with multiple-choice questions.
 * Allows the user to input questions, answers, and select the correct one.
 * After saving, it updates the parent {@link QuizSelection} window.
 */
public class QuizErstellenGUI extends JFrame {

    private final JTextField quizNameField, questionField;
    private final JTextField[] answerField = new JTextField[4];
    private final JRadioButton[] radioButtons = new JRadioButton[4];
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final QuizErstellen quizManager = new QuizErstellen();
    private final JTextArea statusArea;
    private final QuizSelection parent;

    /**
     * Constructs the GUI for quiz creation.
     *
     * @param parent The parent QuizSelection window to refresh after saving.
     */
    public QuizErstellenGUI(QuizSelection parent) {
        this.parent = parent;

        setTitle("Quiz erstellen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 500));
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // 🔝 Top Panel for quiz name input
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.insets = new Insets(5, 5, 5, 5);
        gbcTop.fill = GridBagConstraints.HORIZONTAL;
        gbcTop.weightx = 1.0;

        gbcTop.gridx = 0;
        gbcTop.gridy = 0;
        topPanel.add(new JLabel("Quiz-Name (.txt):"), gbcTop);

        gbcTop.gridx = 1;
        quizNameField = new JTextField();
        topPanel.add(quizNameField, gbcTop);

        add(topPanel, BorderLayout.NORTH);

        // 🧩 Center Panel for question and answer input
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Frage:"), gbc);

        gbc.gridx = 1;
        questionField = new JTextField();
        gbc.weighty = 0.1;
        centerPanel.add(questionField, gbc);

        for (int i = 0; i < 4; i++) {
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.weighty = 0;
            centerPanel.add(new JLabel("Antwort " + (i + 1) + ":"), gbc);

            gbc.gridx = 1;
            answerField[i] = new JTextField();
            radioButtons[i] = new JRadioButton("Richtige Antwort");
            buttonGroup.add(radioButtons[i]);

            JPanel antwortPanel = new JPanel(new BorderLayout());
            antwortPanel.add(answerField[i], BorderLayout.CENTER);
            antwortPanel.add(radioButtons[i], BorderLayout.EAST);
            centerPanel.add(antwortPanel, gbc);
        }

        // 🔘 Buttons for adding question and saving quiz
        gbc.gridy++;
        gbc.gridx = 0;
        JButton addButton = new JButton("Frage hinzufügen");
        centerPanel.add(addButton, gbc);

        gbc.gridx = 1;
        JButton saveButton = new JButton("Quiz speichern");
        centerPanel.add(saveButton, gbc);

        JScrollPane centerScroll = new JScrollPane(centerPanel);
        centerScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(centerScroll, BorderLayout.CENTER);

        // 📜 Status area for messages
        statusArea = new JTextArea(5, 20);
        statusArea.setEditable(false);
        JScrollPane statusScroll = new JScrollPane(statusArea);
        statusScroll.setPreferredSize(new Dimension(100, 100));
        add(statusScroll, BorderLayout.SOUTH);

        // 🎯 Action listeners

        // Adds a new question to the quiz
        addButton.addActionListener(e -> {
            String question = questionField.getText().trim();
            if (question.isEmpty()) {
                zeigeStatus("Fragetext darf nicht leer sein.");
                return;
            }

            ArrayList<String> answers = new ArrayList<>();
            int richtige = -1;

            for (int i = 0; i < 4; i++) {
                String antwort = answerField[i].getText().trim();
                if (antwort.isEmpty()) {
                    zeigeStatus("Antwortfeld " + (i + 1) + " ist leer.");
                    return;
                }
                answers.add(antwort);
                if (radioButtons[i].isSelected()) {
                    richtige = i;
                }
            }

            if (richtige == -1) {
                zeigeStatus("Bitte richtige Antwort auswählen.");
                return;
            }

            Questions neueFrage = new Questions(question, answers.toArray(new String[0]), richtige);
            quizManager.addQuestion(neueFrage);
            zeigeStatus("Frage hinzugefügt.");

            questionField.setText("");
            for (JTextField feld : answerField) feld.setText("");
            buttonGroup.clearSelection();
        });

        // Saves the quiz to file and updates the QuizSelection window
        saveButton.addActionListener(e -> {
            String name = quizNameField.getText().trim();
            if (name.isEmpty()) {
                zeigeStatus("Bitte Dateiname angeben.");
                return;
            }

            String fullFilename = name + "." + Session.getUsername() + ".txt";
            try {
                quizManager.saveQuiz(fullFilename);
                zeigeStatus("Quiz gespeichert unter: C:/temp/Quiz/" + fullFilename);

                if (parent != null) {
                    parent.refreshContent(); // Refresh the parent QuizSelection window
                }

                Window currentWindow = SwingUtilities.getWindowAncestor(saveButton);
                if (currentWindow != null) {
                    currentWindow.dispose(); // Close this window
                }

            } catch (IOException ex) {
                zeigeStatus("Fehler beim Speichern: " + ex.getMessage());
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Set default button for Enter key
        centerPanel.getRootPane().setDefaultButton(addButton);
    }

    /**
     * Appends a message to the status area.
     *
     * @param msg Message to display.
     */
    private void zeigeStatus(String msg) {
        statusArea.append(msg + "\n");
    }
}
