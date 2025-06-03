package src;

import java.util.Scanner;

/**
 * Behandelt die Korrektur und Validierung von Quizfragen-Antworten.
 * Bietet Funktionalität, um Benutzerantworten gegen richtige Antworten zu überprüfen und den Antwortstatus zu verfolgen.
 */
public class Correction {

    /**
     * Hauptmethode, die die Korrekturfunktionalität mit einer Beispielfrage demonstriert.
     *
     * @param args Kommandozeilenargumente (nicht verwendet)
     */
    public static void main(String[] args) {
        QuestionCorrection question = new QuestionCorrection(
                "How many planets are there in our solar system?",
                "8"
        );
        Correction correction = new Correction();
        correction.checkAnswer(question);
    }

    /** Flag, um zu verfolgen, ob diese Frage bereits beantwortet wurde */
    private boolean answered = false;

    /**
     * Überprüft die Antwort eines Benutzers gegen die richtige Antwort für eine gegebene Frage.
     * Behandelt die Eingabevalidierung, Antwortüberprüfung und verhindert mehrere Versuche.
     * Der Scanner wird absichtlich nicht geschlossen, um System.in nicht zu schließen.
     *
     * @param question das QuestionCorrection-Objekt, das die Frage und die richtige Antwort enthält
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
