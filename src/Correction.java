package src;

import org.json.JSONObject;

import java.util.Scanner;

public class Correction {
    public Correction(String[] args) {
        QuestionCorrection question = new QuestionCorrection("How many planets are there in our solar system?", "8");
        checkAnswer(question);
    }

    private boolean answered = false;

    public void checkAnswer(QuestionCorrection question) {
        if (question == null) {
            System.out.println("⚠️ No question available.");
            return;
        }

        System.out.println("Question: " + question.text);
        System.out.println("Please enter your answer:");

        Scanner scanner = new Scanner(System.in);
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
        scanner.close();
    }

    private int getCorrectAnswer() {
        Scanner reader = new Scanner("questions.txt");
        String line = reader.nextLine();

        JSONObject obj = new JSONObject(line);

        return (int) obj.get("correct");
    }
}
