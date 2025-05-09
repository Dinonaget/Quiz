package src;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    public QuizGUI() {
        loadQuestionsFromFile("questions.txt"); // Lade Fragen aus der Datei

        if (questionsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Fragen gefunden!"); // Fehler, wenn keine Fragen vorhanden sind
            System.exit(1); // Beende das Programm
        }

        setupGUI(); // Setze das GUI auf
        showQuestion(); // Zeige die erste Frage
    }

    private void setupGUI() {
        setTitle("Quiz App"); // Setze den Titel des Fensters
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Beende das Programm beim Schließen
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
        nextButton.addActionListener(e -> checkAndNext()); // Event-Listener für den Button
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
        System.exit(0); // Beende das Programm
    }

    private void loadQuestionsFromFile(String filename) {
        try {
            // Lese alle Zeilen der Datei
            for (String line : Files.readAllLines(Paths.get(filename))) {
                try {
                    // Parse jede Zeile als JSON
                    JSONObject obj = new JSONObject(line);

                    String question = obj.getString("question");
                    JSONArray answersJSON = obj.getJSONArray("answers");
                    int correct = obj.getInt("correct");

                    // Überprüfe, ob genau 4 Antworten vorhanden sind
                    if (answersJSON.length() != 4) {
                        System.out.println("⚠️ Frage übersprungen (nicht genau 4 Antworten): " + line);
                        continue; // Überspringe diese Frage, wenn sie nicht korrekt ist
                    }

                    // Speichere die Antworten in einem Array
                    String[] answers = new String[4];
                    for (int i = 0; i < 4; i++) {
                        answers[i] = answersJSON.getString(i);
                    }

                    // Erstelle eine neue Frage und füge sie der Liste hinzu
                    Questions q = new Questions(question, answers, correct);
                    questionsList.add(q);
                } catch (Exception e) {
                    System.out.println("⚠️ Ungültige Zeile in questions.txt übersprungen:\n" + line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Fehler beim Lesen der Datei: " + e.getMessage());
            System.exit(1); // Beende das Programm bei Fehler
        }
    }
}
