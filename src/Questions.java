package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stellt eine Multiple-Choice-Frage mit vier möglichen Antworten dar und verfolgt die richtige Antwort.
 * Jeder Frage wird bei der Erstellung automatisch eine eindeutige ID zugewiesen.
 */
public class Questions {

    /** Statischer ID-Generator, der sicherstellt, dass jede Frage eine eindeutige sequentielle ID erhält, beginnend bei 1 */
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    /** Die eindeutige numerische Kennung für diese Frage */
    private final int id;

    /** Der Textinhalt der Frage */
    private String question;

    /** Array, das vier mögliche Antworten für die Frage enthält */
    private String[] answers = new String[4];

    /** Index der richtigen Antwort im Antworten-Array (0-3) */
    private int correctIndex;

    /**
     * Erstellt eine neue Frage mit dem angegebenen Inhalt und weist automatisch eine eindeutige ID zu.
     *
     * @param question der Text der Frage
     * @param answers Array von vier möglichen Antworten
     * @param correctIndex der Index (0-3) der richtigen Antwort im Antworten-Array
     */
    public Questions(String question, String[] answers, int correctIndex) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.question = question;
        this.answers = answers;
        this.correctIndex = correctIndex;
    }

    /**
     * Gibt die eindeutige ID dieser Frage zurück.
     *
     * @return die numerische ID der Frage
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt den Textinhalt der Frage zurück.
     *
     * @return der Fragetext
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Gibt das Array der möglichen Antworten für diese Frage zurück.
     *
     * @return Array, das vier mögliche Antworten enthält
     */
    public String[] getAnswers() {
        return answers;
    }

    /**
     * Gibt den Index der richtigen Antwort zurück.
     *
     * @return der Index (0-3) der richtigen Antwort im Antworten-Array
     */
    public int getCorrectIndex() {
        return correctIndex;
    }

    /**
     * Setzt den Textinhalt der Frage.
     *
     * @param question der neue Fragetext
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Setzt das Array der möglichen Antworten für diese Frage.
     *
     * @param answers Array von vier möglichen Antworten
     */
    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    /**
     * Setzt den Index der richtigen Antwort.
     *
     * @param correctIndex der Index (0-3) der richtigen Antwort im Antworten-Array
     */
    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    /**
     * Schreibt die Fragendaten in eine Datei in einem bestimmten Format.
     * Erstellt die Verzeichnisstruktur, falls sie nicht existiert.
     * Das Format umfasst: ID, Fragetext, vier Antworten und den Index der richtigen Antwort.
     *
     * @param filename der Name der Datei, in die geschrieben werden soll (wird in C:/temp/Quiz/ abgelegt)
     * @throws IOException wenn ein Fehler beim Schreiben oder Erstellen des Verzeichnisses auftritt
     */
    public void writeToFile(String filename) throws IOException {
        String dirPath = "C:/temp/Quiz";
        java.nio.file.Path directory = java.nio.file.Paths.get(dirPath);

        if (!java.nio.file.Files.exists(directory)) {
            java.nio.file.Files.createDirectories(directory);
        }

        String fullPath = dirPath + "/" + filename;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath, true))) {
            writer.write("ID:" + id);
            writer.newLine();
            writer.write(question);
            writer.newLine();
            for (String answer : answers) {
                writer.write(answer);
                writer.newLine();
            }
            writer.write(Integer.toString(correctIndex));
            writer.newLine();
        }
    }
}
