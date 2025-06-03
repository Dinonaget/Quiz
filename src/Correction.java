package src;

import java.util.Scanner;

/**
 * Handles the correction and validation of quiz question answers.
 * Provides functionality to check user answers against correct answers and track answer status.
 */
public class Correction {

    /**
     * Main method that demonstrates the correction functionality with a sample question.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        QuestionCorrection question = new QuestionCorrection(
                "How many planets are there in our solar system?",
                "8"
        );
        Correction correction = new Correction();
        correction.checkAnswer(question);
    }

    /** Flag to track whether this question has already been answered */
    private boolean answered = false;

    /**
     * Checks a user's answer against the correct answer for a given question.
     * Handles input validation, answer verification, and prevents multiple attempts.
     * The Scanner is intentionally not closed to avoid closing System.in.
     *
     * @param question the QuestionCorrection object containing the question and correct answer
     */
    public void checkAnswer(QuestionCorrection question) {
        if (question == null) {
            System.out.println("No question available.");
            return;
        }

        System.out.println("Question: " + question.text);
        System.out.println("Please enter your answer:");

        Scanner scanner = new Scanner(System.in);
        String userAnswer = scanner.nextLine().trim();

        if (userAnswer.isEmpty()) {
            System.out.println("No answer provided. Please try again.");
            return;
        }

        if (answered) {
            System.out.println("You already answered this question.");
            return;
        }

        if (question.isCorrect(userAnswer)) {
            System.out.println("Correct!");
        } else {
            System.out.println("Incorrect!");
            System.out.println("The correct answer is: " + question.correctAnswer);
        }

        answered = true;
    }
}