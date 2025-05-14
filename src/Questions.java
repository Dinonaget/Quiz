package src;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * The {@code Questions} class represents a multiple-choice question with a set of possible answers
 * and the index of the correct answer.
 */
public class Questions {
    private String question;
    private String[] answers = new String[4];
    private int correctIndex;

    /**
     * Constructs a new {@code Questions} object.
     *
     * @param question     the text of the question
     * @param answers      an array of four possible answers
     * @param correctIndex the index of the correct answer (0-based)
     */
    public Questions(String question, String[] answers, int correctIndex) {
        this.question = question;
        this.answers = answers;
        this.correctIndex = correctIndex;
    }

    /**
     * Returns the question text.
     *
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Returns the array of possible answers.
     *
     * @return the answers
     */
    public String[] getAnswers() {
        return answers;
    }

    /**
     * Returns the index of the correct answer.
     *
     * @return the correct answer index
     */
    public int getCorrectIndex() {
        return correctIndex;
    }

    /**
     * Schreibt die Frage, ihre vier Antwortmöglichkeiten und den Index der korrekten Antwort
     * als einfache Textzeilen in eine Datei im Verzeichnis {@code C:/temp/Quiz}.
     *
     * <p>Die Datei wird im Anfügemodus geöffnet, sodass neue Fragen am Ende hinzugefügt werden,
     * ohne vorhandene zu überschreiben. Jede Frage wird dabei in sechs Zeilen geschrieben:
     * <ol>
     *     <li>Die Frage selbst</li>
     *     <li>Antwort 1</li>
     *     <li>Antwort 2</li>
     *     <li>Antwort 3</li>
     *     <li>Antwort 4</li>
     *     <li>Index der richtigen Antwort (0-basiert)</li>
     * </ol>
     *
     * <p>Falls das Zielverzeichnis {@code C:/temp/Quiz} nicht existiert, wird es automatisch erstellt.</p>
     *
     * @param filename Der Name der Datei, in die geschrieben werden soll (z. B. {@code "questions.txt"}).
     * @throws IOException Falls beim Schreiben in die Datei ein Fehler auftritt.
     */
    public void writeToFile(String filename) throws IOException {
        // Zielverzeichnis festlegen
        String dirPath = "C:/temp/Quiz";
        java.nio.file.Path directory = java.nio.file.Paths.get(dirPath);

        // Verzeichnis erstellen, falls es nicht existiert
        if (!java.nio.file.Files.exists(directory)) {
            java.nio.file.Files.createDirectories(directory);
        }

        // Pfad zur Datei
        String fullPath = dirPath + "/" + filename;

        // Schreiben in die Datei im Anfügemodus
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath, true))) {
            writer.write(question);
            writer.newLine();
            for (int i = 0; i < 4; i++) {
                writer.write(answers[i]);
                writer.newLine();
            }
            writer.write(Integer.toString(correctIndex));
            writer.newLine();
        }
    }

}