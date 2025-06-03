package src;

/**
 * Represents a quiz question with its correct answer and provides functionality
 * to validate user responses against the correct answer.
 */
public class QuestionCorrection {

    /** The text content of the question */
    String text;

    /** The correct answer for this question */
    String correctAnswer;

    /**
     * Constructs a new QuestionCorrection with the specified question text and correct answer.
     *
     * @param text the question text
     * @param correctAnswer the correct answer for this question
     */
    public QuestionCorrection(String text, String correctAnswer) {
        this.text = text;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Checks if the provided user answer matches the correct answer.
     * The comparison is case-insensitive and handles null input safely.
     *
     * @param userAnswer the answer provided by the user
     * @return true if the user answer matches the correct answer (case-insensitive), false otherwise
     */
    public boolean isCorrect(String userAnswer) {
        return userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer);
    }
}