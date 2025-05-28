package src;

/**
 * Die Klasse QuestionCorrection repräsentiert eine Frage und die dazugehörige korrekte Antwort.
 * Sie bietet eine Methode zur Überprüfung, ob eine gegebene Antwort korrekt ist.
 */
public class QuestionCorrection {
    String text;
    String correctAnswer;

    /**
     * Konstruktor für die Erstellung einer neuen Frage mit der dazugehörigen korrekten Antwort.
     *
     * @param text Der Text der Frage.
     * @param correctAnswer Die korrekte Antwort auf die Frage.
     */
    public QuestionCorrection(String text, String correctAnswer) {
        this.text = text;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Überprüft, ob die gegebene Antwort des Benutzers korrekt ist.
     *
     * @param userAnswer Die Antwort des Benutzers.
     * @return true, wenn die Antwort korrekt ist, sonst false.
     */
    public boolean isCorrect(String userAnswer) {
        return userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer);
    }
}
