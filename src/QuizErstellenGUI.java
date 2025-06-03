package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * GUI zum Erstellen eines neuen Quiz mit Multiple-Choice-Fragen.
 * Ermöglicht dem Benutzer, Fragen, Antworten einzugeben und die richtige Antwort auszuwählen.
 * Nach dem Speichern wird das übergeordnete QuizSelection-Fenster aktualisiert.
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
     * Erstellt die GUI zur Quiz-Erstellung.
     *
     * @param parent Das übergeordnete QuizSelection-Fenster, das nach dem Speichern aktualisiert wird.
     */
    public QuizErstellenGUI(QuizSelection parent) {
        this.parent = parent;

        setTitle("Quiz erstellen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 500));
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        /**
         * Oberes Panel für die Eingabe des Quiz-Namens
         */
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

        /**
         * Mittleres Panel für die Eingabe von Fragen und Antworten
         */
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

        /**
         * Schaltflächen zum Hinzufügen von Fragen und Speichern des Quiz
         */
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

        /**
         * Statusbereich zur Anzeige von Nachrichten
         */
        statusArea = new JTextArea(5, 20);
        statusArea.setEditable(false);
        JScrollPane statusScroll = new JScrollPane(statusArea);
        statusScroll.setPreferredSize(new Dimension(100, 100));
        add(statusScroll, BorderLayout.SOUTH);

        /**
         * Einrichtung der Aktions-Listener
         */

        /**
         * Fügt eine neue Frage zum Quiz hinzu
         */
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

        /**
         * Speichert das Quiz in einer Datei und aktualisiert das QuizSelection-Fenster
         */
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
                    parent.refreshContent();
                }

                Window currentWindow = SwingUtilities.getWindowAncestor(saveButton);
                if (currentWindow != null) {
                    currentWindow.dispose();
                }

            } catch (IOException ex) {
                zeigeStatus("Fehler beim Speichern: " + ex.getMessage());
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        /**
         * Setze Standard-Schaltfläche für die Eingabetaste
         */
        centerPanel.getRootPane().setDefaultButton(addButton);
    }

    /**
     * Fügt eine Nachricht zum Statusbereich hinzu.
     *
     * @param msg Nachricht, die angezeigt werden soll.
     */
    private void zeigeStatus(String msg) {
        statusArea.append(msg + "\n");
    }
}
