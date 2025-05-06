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
     * Schreibt die Frage, ihre Antworten und den Index der richtigen Antwort in eine Datei im JSON-Format.
     *
     * <p>Die Datei wird im Anfügemodus geöffnet, sodass neue Einträge nicht vorhandene überschreiben.</p>
     *
     * @param filename Der Name der Datei, in die geschrieben werden soll.
     * @throws IOException Falls ein Fehler beim Datei-Schreiben auftritt.
     */
    public void writeToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            JSONObject obj = new JSONObject();
            obj.put("question", question);

            JSONArray answer = new JSONArray();

            answer.put(answers[0]);
            answer.put(answers[1]);
            answer.put(answers[2]);
            answer.put(answers[3]);

            obj.put("answers", answer);

            obj.put("correct", correctIndex);

            writer.write(obj.toString());

            writer.newLine();
        }
    }

    /**
     * Returns a string representation of this {@code Questions} object.
     *
     * @return a string describing the question
     */
    @Override
    public String toString() {
        return "Questions{" +
                "question='" + question + '\'' +
                ", answers=" + Arrays.toString(answers) +
                ", correctIndex=" + correctIndex +
                '}';
    }
}