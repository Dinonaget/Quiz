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
        setMinimumSize(new Dimension(600, 500));
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // 🔝 Top Panel
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.insets = new Insets(5, 5, 5, 5);
        gbcTop.fill = GridBagConstraints.HORIZONTAL;
        gbcTop.weightx = 1.0;

        gbcTop.gridx = 0; gbcTop.gridy = 0;
        topPanel.add(new JLabel("Quiz-Name (.txt):"), gbcTop);

        gbcTop.gridx = 1;
        quizNameField = new JTextField();
        topPanel.add(quizNameField, gbcTop);

        gbcTop.gridx = 0; gbcTop.gridy = 1;
        JButton createButton = new JButton("Quiz erstellen");
        topPanel.add(createButton, gbcTop);

        gbcTop.gridx = 1;
        JButton loadButton = new JButton("Quiz laden");
        topPanel.add(loadButton, gbcTop);

        add(topPanel, BorderLayout.NORTH);

        // 🧩 Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
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

        // 📜 Statusbereich
        statusArea = new JTextArea(5, 20);
        statusArea.setEditable(false);
        JScrollPane statusScroll = new JScrollPane(statusArea);
        statusScroll.setPreferredSize(new Dimension(100, 100));
        add(statusScroll, BorderLayout.SOUTH);

        // 🎯 Actions
        createButton.addActionListener(e -> {
            String name = quizNameField.getText().trim();
            if (name.isEmpty()) {
                zeigeStatus("Fehler: Quizname darf nicht leer sein.");
            } else {
                quizManager = new QuizErstellen();
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

        pack();
        setLocationRelativeTo(null); // zentriert
        setVisible(true);
        centerPanel.getRootPane().setDefaultButton(addButton);
    }

    private void zeigeStatus(String msg) {
        statusArea.append(msg + "\n");
    }
}
