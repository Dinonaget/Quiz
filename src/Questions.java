package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a multiple-choice question with four possible answers and tracks the correct answer.
 * Each question is assigned a unique ID automatically upon creation.
 */
public class Questions {

    /** Static ID generator that ensures each question gets a unique sequential ID starting from 1 */
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    /** The unique numerical identifier for this question */
    private final int id;

    /** The text content of the question */
    private String question;

    /** Array containing four possible answers for the question */
    private String[] answers = new String[4];

    /** Index of the correct answer in the answers array (0-3) */
    private int correctIndex;

    /**
     * Constructs a new question with the specified content and automatically assigns a unique ID.
     *
     * @param question the text of the question
     * @param answers array of four possible answers
     * @param correctIndex the index (0-3) of the correct answer in the answers array
     */
    public Questions(String question, String[] answers, int correctIndex) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.question = question;
        this.answers = answers;
        this.correctIndex = correctIndex;
    }

    /**
     * Gets the unique ID of this question.
     *
     * @return the numerical ID of the question
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the text content of the question.
     *
     * @return the question text
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Gets the array of possible answers for this question.
     *
     * @return array containing four possible answers
     */
    public String[] getAnswers() {
        return answers;
    }

    /**
     * Gets the index of the correct answer.
     *
     * @return the index (0-3) of the correct answer in the answers array
     */
    public int getCorrectIndex() {
        return correctIndex;
    }

    /**
     * Sets the text content of the question.
     *
     * @param question the new question text
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets the array of possible answers for this question.
     *
     * @param answers array of four possible answers
     */
    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    /**
     * Sets the index of the correct answer.
     *
     * @param correctIndex the index (0-3) of the correct answer in the answers array
     */
    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    /**
     * Writes the question data to a file in a specific format.
     * Creates the directory structure if it doesn't exist.
     * The format includes: ID, question text, four answers, and correct index.
     *
     * @param filename the name of the file to write to (will be placed in C:/temp/Quiz/)
     * @throws IOException if an error occurs during file writing or directory creation
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