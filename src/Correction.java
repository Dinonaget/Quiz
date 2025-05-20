package src;

import java.util.Scanner;

public class Correction {
    public static void main(String[] args) {
        QuestionCorrection question = new QuestionCorrection(
                "How many planets are there in our solar system?",
                "8"
        );
        Correction correction = new Correction();
        correction.checkAnswer(question);
    }

    private boolean answered = false;

    public void checkAnswer(QuestionCorrection question) {
        if (question == null) {
            System.out.println("⚠️ No question available.");
            return;
        }

        System.out.println("Question: " + question.text);
        System.out.println("Please enter your answer:");

        Scanner scanner = new Scanner(System.in); // NICHT schließen
        String userAnswer = scanner.nextLine().trim();

        if (userAnswer.isEmpty()) {
            System.out.println("⚠️ No answer provided. Please try again.");
            return;
        }

        if (answered) {
            System.out.println("❗ You already answered this question.");
            return;
        }

        if (question.isCorrect(userAnswer)) {
            System.out.println("✅ Correct!");
        } else {
            System.out.println("❌ Incorrect!");
            System.out.println("The correct answer is: " + question.correctAnswer);
        }

        answered = true;
        // scanner.close();  ← NICHT schließen
    }
}
