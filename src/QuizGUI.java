package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class QuizGUI extends JFrame {
    private ArrayList<Questions> questionsList = new ArrayList<>(); // Liste der Fragen
    private int currentIndex = 0; // Index der aktuellen Frage
    private int score = 0; // Punktzahl des Benutzers

    private JLabel questionLabel; // Label für die Frage
    private JRadioButton[] options; // Radiobuttons für die Antworten
    private ButtonGroup group; // Gruppe für die Radiobuttons
    private JButton nextButton; // Button für die nächste Frage

    public QuizGUI(String filename) {
//        loadQuestionsFromFile("questions.txt"); // Lade Fragen aus der Datei
            loadQuestionsFromFile(filename); // statt fix "questions.txt"
            // ... Rest bleibt gleich


        if (questionsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Fragen gefunden!"); // Fehler, wenn keine Fragen vorhanden sind
            System.exit(1); // Beende das Programm
        }

        setupGUI(); // Setze das GUI auf
        showQuestion(); // Zeige die erste Frage
    }

    private void setupGUI() {
        setTitle("Quiz App"); // Setze den Titel des Fensters
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Beende das Programm beim Schließen
        setSize(500, 300); // Fenstergröße
        setLocationRelativeTo(null); // Fenster zentrieren
        setLayout(new BorderLayout()); // Layout des Fensters

        // Frage-Label
        questionLabel = new JLabel("Frage", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Schriftart
        add(questionLabel, BorderLayout.NORTH);

        // Panel für die Antworten
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        options = new JRadioButton[4];
        group = new ButtonGroup(); // Gruppe für die Radiobuttons

        // Erstelle 4 Radiobuttons für die Antworten
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]); // Füge den Button zur Gruppe hinzu
            optionsPanel.add(options[i]); // Füge den Button zum Panel hinzu
        }

        add(optionsPanel, BorderLayout.CENTER); // Panel zum Fenster hinzufügen

        // Button für die nächste Frage
        nextButton = new JButton("Weiter");
        nextButton.addActionListener(e -> checkAndNext());// Event-Listener für den Button
        getRootPane().setDefaultButton(nextButton);
        add(nextButton, BorderLayout.SOUTH); // Button zum Fenster hinzufügen

        setVisible(true); // Mache das Fenster sichtbar
    }

    private void showQuestion() {
        if (currentIndex >= questionsList.size()) {
            showResult(); // Zeige das Ergebnis, wenn alle Fragen beantwortet sind
            return;
        }

        // Hole die aktuelle Frage
        Questions q = questionsList.get(currentIndex);
        questionLabel.setText("Frage " + (currentIndex + 1) + ": " + q.getQuestion()); // Zeige die Frage an

        // Setze die Antwortmöglichkeiten
        String[] answers = q.getAnswers();
        for (int i = 0; i < answers.length; i++) {
            options[i].setText(answers[i]);
            options[i].setSelected(false); // Setze alle Radiobuttons auf nicht ausgewählt
        }
    }

    private void checkAndNext() {
        int selected = -1;
        // Prüfe, welche Antwort ausgewählt wurde
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                selected = i;
                break;
            }
        }

        // Falls keine Antwort ausgewählt wurde
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Bitte eine Antwort auswählen.");
            return;
        }

        // Hole die aktuelle Frage
        Questions q = questionsList.get(currentIndex);
        if (selected == q.getCorrectIndex()) { // Wenn die Antwort richtig war
            score++; // Erhöhe die Punktzahl
        }

        currentIndex++; // Gehe zur nächsten Frage

        // Überprüfe, ob das Quiz beendet ist
        if (currentIndex >= questionsList.size()) {
            showResult(); // Zeige das Ergebnis an
        } else {
            showQuestion(); // Zeige die nächste Frage
        }
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this, "✅ Du hast " + score + " von " + questionsList.size() + " richtig!");
        dispose(); // Beende das Programm
    }

    /**
     * Lädt Fragen aus einer Textdatei im Verzeichnis {@code C:/temp/Quiz} und speichert sie in der internen Liste.
     *
     * <p>Die Datei muss das folgende Format haben, wobei jede Frage genau 6 Zeilen einnimmt:</p>
     * <ol>
     *     <li>Fragetext</li>
     *     <li>Antwort 1</li>
     *     <li>Antwort 2</li>
     *     <li>Antwort 3</li>
     *     <li>Antwort 4</li>
     *     <li>Index der richtigen Antwort (0-basiert)</li>
     * </ol>
     *
     * <p>Falls der Index ungültig ist oder ein Formatfehler vorliegt, wird die betreffende Frage übersprungen.</p>
     *
     * @param filename Der Name der Datei (z. B. {@code "questions.txt"}), die sich im Verzeichnis {@code C:/temp/Quiz} befindet.
     */
    private void loadQuestionsFromFile(String filename) {
        String fullPath = "C:/temp/Quiz/" + filename; // Absoluter Pfad zur Datei
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
                    if (correct < 0 || correct > 3) {
                        System.out.println("⚠️ Ungültiger Index bei Frage: " + question);
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Ungültiger Zahlenwert für korrekte Antwort bei Frage: " + question);
                    continue;
                }

                Questions q = new Questions(question, answers, correct);
                questionsList.add(q);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Fehler beim Lesen der Datei:\n" + fullPath + "\n" + e.getMessage());
            System.exit(1);
        }
    }
}
