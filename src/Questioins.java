package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * The {@code Questioins} class represents a multiple-choice question with a set of possible answers
 * and the index of the correct answer.
 */
public class Questioins {
    private String question;
    private String[] answers = new String[4];
    private int correctIndex;

    /**
     * Constructs a new {@code Questioins} object.
     *
     * @param question     the text of the question
     * @param answers      an array of four possible answers
     * @param correctIndex the index of the correct answer (0-based)
     */
    public Questioins(String question, String[] answers, int correctIndex) {
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
     * Writes this question object to a file.
     *
     * @param filename the file to write to
     * @throws IOException if an I/O error occurs
     */
    public void writeToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write("Question: " + question);
            writer.newLine();
            for (int i = 0; i < answers.length; i++) {
                writer.write((i + 1) + ". " + answers[i]);
                writer.newLine();
            }
            writer.write("Correct Answer Index: " + correctIndex);
            writer.newLine();
            writer.write("----");
            writer.newLine();
        }
    }

    /**
     * Returns a string representation of this {@code Questioins} object.
     *
     * @return a string describing the question
     */
    @Override
    public String toString() {
        return "Questioins{" +
                "question='" + question + '\'' +
                ", answers=" + Arrays.toString(answers) +
                ", correctIndex=" + correctIndex +
                '}';
    }
}