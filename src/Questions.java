package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Questions {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1); // Einfache ID (beginnend bei 1)

    private final int id;  // numerische ID
    private String question;
    private String[] answers = new String[4];
    private int correctIndex;

    /**
     * Konstruktor für eine neue Frage
     */
    public Questions(String question, String[] answers, int correctIndex) {
        this.id = ID_GENERATOR.getAndIncrement(); // automatische Vergabe einer eindeutigen ID
        this.question = question;
        this.answers = answers;
        this.correctIndex = correctIndex;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    /**
     * Schreibt die Frage mit ID in die Datei
     */
    public void writeToFile(String filename) throws IOException {
        String dirPath = "C:/temp/Quiz";
        java.nio.file.Path directory = java.nio.file.Paths.get(dirPath);

        if (!java.nio.file.Files.exists(directory)) {
            java.nio.file.Files.createDirectories(directory);
        }

        String fullPath = dirPath + "/" + filename;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath, true))) {
            writer.write("ID:" + id);  // einfache ID als erste Zeile
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
