package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class QuizErstellenGUI extends JFrame {
    private JTextField quizNameField, questionField;
    private JTextField[] answerField = new JTextField[4];
    private JRadioButton[] radioButtons = new JRadioButton[4];
    private ButtonGroup buttonGroup = new ButtonGroup();
    private QuizErstellen quizManager = new QuizErstellen();
    private JTextArea statusArea;

    public QuizErstellenGUI() {
        setTitle("Quiz erstellen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        topPanel.add(new JLabel("Quiz-Name (.txt):"));
        quizNameField = new JTextField();
        topPanel.add(quizNameField);
        JButton createButton = new JButton("Quiz erstellen");
        topPanel.add(createButton);
        JButton loadButton = new JButton("Quiz laden");
        topPanel.add(loadButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(7, 2));
        questionField = new JTextField();
        centerPanel.add(new JLabel("Frage:"));
        centerPanel.add(questionField);

        for (int i = 0; i < 4; i++) {
            answerField[i] = new JTextField();
            radioButtons[i] = new JRadioButton("Richtige Antwort");
            buttonGroup.add(radioButtons[i]);
            centerPanel.add(new JLabel("Antwort " + (i + 1) + ":"));
            JPanel antwortPanel = new JPanel(new BorderLayout());
            antwortPanel.add(answerField[i], BorderLayout.CENTER);
            antwortPanel.add(radioButtons[i], BorderLayout.EAST);
            centerPanel.add(antwortPanel);
        }

        JButton addButton = new JButton("Frage hinzufügen");
        centerPanel.add(addButton);
        JButton saveButton = new JButton("Quiz speichern");
        centerPanel.add(saveButton);
        add(centerPanel, BorderLayout.CENTER);

        statusArea = new JTextArea(5, 20);
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.SOUTH);

        // ActionListener
        createButton.addActionListener(e -> {
            String name = quizNameField.getText().trim();
            if (name.isEmpty()) {
                zeigeStatus("Fehler: Quizname darf nicht leer sein.");
            } else {
                quizManager = new QuizErstellen(); // Neue Instanz leeren
                zeigeStatus("Neues Quiz gestartet: " + name + ".txt");
            }
        });

        loadButton.addActionListener(e -> {
            String name = quizNameField.getText().trim();
            if (name.isEmpty()) {
                zeigeStatus("Dateiname eingeben zum Laden.");
                return;
            }

            String fullFilename = name + "." + Session.getUsername() + ".txt";
            try {
                quizManager.loadQuiz(fullFilename);
                zeigeStatus("Quiz geladen: " + fullFilename);
            } catch (IOException ex) {
                zeigeStatus("Fehler beim Laden: " + ex.getMessage());
            }
        });


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
            } catch (IOException ex) {
                zeigeStatus("Fehler beim Speichern: " + ex.getMessage());
            }
        });


        setVisible(true);
    }

    private void zeigeStatus(String msg) {
        statusArea.append(msg + "\n");
    }
}
