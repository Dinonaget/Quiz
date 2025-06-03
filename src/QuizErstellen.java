package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The QuizErstellen class manages a list of questions and provides
 * methods for saving and loading questions to/from a file.
 *
 * Questions are stored in the directory C:/temp/Quiz.
 */
public class QuizErstellen {
    /** List to store all questions in the quiz */
    private List<Questions> questionList = new ArrayList<>();

    /**
     * Adds a new question to the internal list.
     *
     * @param question The question to be added
     */
    public void addQuestion(Questions question) {
        questionList.add(question);
    }

    /**
     * Returns the list of all stored questions.
     *
     * @return List of Questions objects
     */
    public List<Questions> getQuestions() {
        return questionList;
    }

    /**
     * Saves all questions to a file in the C:/temp/Quiz directory.
     * Each question is saved in append mode.
     *
     * @param filename The filename (e.g., "questions.txt")
     * @throws IOException If an error occurs while writing
     */
    public void saveQuiz(String filename) throws IOException {
        // Iterate through all questions and save each one to file
        for (Questions frage : questionList) {
            frage.writeToFile(filename);
        }
    }

    /**
     * Loads all questions from a file in the C:/temp/Quiz directory.
     * The file must be in the correct format (6 lines per question).
     *
     * @param filename The filename (e.g., "questions.txt")
     * @throws IOException If an error occurs while reading
     */
    public void loadQuiz(String filename) throws IOException {
        // Clear existing questions before loading new ones
        questionList.clear();

        // Construct full file path
        String dirPath = "C:/temp/Quiz";
        String fullPath = dirPath + "/" + filename;

        // Read file using BufferedReader with try-with-resources
        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Line 1: Read ID line (optional usage, currently ignored)
                String idLine = line;
                if (!idLine.startsWith("ID:")) {
                    throw new IOException("Unexpected file format: ID line missing.");
                }

                // Line 2: Question text
                String question = reader.readLine();
                if (question == null) throw new IOException("Missing question after ID line.");

                // Lines 3-6: Answer options
                String[] answers = new String[4];
                for (int i = 0; i < 4; i++) {
                    answers[i] = reader.readLine();
                }

                // Line 7: Correct answer index
                int correctIndex = Integer.parseInt(reader.readLine());

                // Create new Question object (ignoring ID since Questions class auto-generates ID)
                Questions q = new Questions(question, answers, correctIndex);
                questionList.add(q);
            }
        }
    }
}