package src;

import java.util.Scanner;

/**
 * Die Klasse Correction ist für die Überprüfung der Antwort des Benutzers auf eine gegebene Frage verantwortlich.
 */
public class Correction {
    /**
     * Die Hauptmethode zum Ausführen des Correction-Programms.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet).
     */
    public static void main(String[] args) {
        QuestionCorrection question = new QuestionCorrection(
                "How many planets are there in our solar system?",
                "8"
        );
        Correction correction = new Correction();
        correction.checkAnswer(question);
    }

    /**
     * Flag, um zu überprüfen, ob die Frage bereits beantwortet wurde.
     */
    private boolean answered = false;

    /**
     * Überprüft die Antwort des Benutzers mit der richtigen Antwort der gegebenen Frage.
     *
     * @param question Die zu beantwortende Frage.
     */
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
