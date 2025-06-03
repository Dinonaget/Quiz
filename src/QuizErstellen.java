package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die QuizErstellen-Klasse verwaltet eine Liste von Fragen und bietet
 * Methoden zum Speichern und Laden von Fragen in/aus einer Datei.
 *
 * Fragen werden im Verzeichnis C:/temp/Quiz gespeichert.
 */
public class QuizErstellen {
    /** Liste zum Speichern aller Fragen im Quiz */
    private List<Questions> questionList = new ArrayList<>();

    /**
     * Fügt eine neue Frage zur internen Liste hinzu.
     *
     * @param question Die hinzuzufügende Frage
     */
    public void addQuestion(Questions question) {
        questionList.add(question);
    }

    /**
     * Gibt die Liste aller gespeicherten Fragen zurück.
     *
     * @return Liste von Questions-Objekten
     */
    public List<Questions> getQuestions() {
        return questionList;
    }

    /**
     * Speichert alle Fragen in einer Datei im Verzeichnis C:/temp/Quiz.
     * Jede Frage wird im Anhängemodus gespeichert.
     *
     * @param filename Der Dateiname (z.B. "questions.txt")
     * @throws IOException Wenn ein Fehler beim Schreiben auftritt
     */
    public void saveQuiz(String filename) throws IOException {
        // Iteriere durch alle Fragen und speichere jede in einer Datei
        for (Questions frage : questionList) {
            frage.writeToFile(filename);
        }
    }

    /**
     * Lädt alle Fragen aus einer Datei im Verzeichnis C:/temp/Quiz.
     * Die Datei muss im richtigen Format vorliegen (6 Zeilen pro Frage).
     *
     * @param filename Der Dateiname (z.B. "questions.txt")
     * @throws IOException Wenn ein Fehler beim Lesen auftritt
     */
    public void loadQuiz(String filename) throws IOException {
        // Lösche bestehende Fragen, bevor neue geladen werden
        questionList.clear();

        // Konstruiere den vollständigen Dateipfad
        String dirPath = "C:/temp/Quiz";
        String fullPath = dirPath + "/" + filename;

        // Lese die Datei mit BufferedReader mit try-with-resources
        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Zeile 1: Lese die ID-Zeile (optionale Verwendung, aktuell ignoriert)
                String idLine = line;
                if (!idLine.startsWith("ID:")) {
                    throw new IOException("Unwartetes Dateiformat: ID-Zeile fehlt.");
                }

                // Zeile 2: Fragetext
                String question = reader.readLine();
                if (question == null) throw new IOException("Fehlende Frage nach ID-Zeile.");

                // Zeilen 3-6: Antwortoptionen
                String[] answers = new String[4];
                for (int i = 0; i < 4; i++) {
                    answers[i] = reader.readLine();
                }

                // Zeile 7: Index der richtigen Antwort
                int correctIndex = Integer.parseInt(reader.readLine());

                // Erstelle ein neues Question-Objekt (ID wird ignoriert, da die Questions-Klasse automatisch eine ID generiert)
                Questions q = new Questions(question, answers, correctIndex);
                questionList.add(q);
            }
        }
    }
}
