package src;

/**
 * Stellt eine Quizfrage mit ihrer richtigen Antwort dar und bietet Funktionalität,
 * um Benutzerantworten gegen die richtige Antwort zu validieren.
 */
public class QuestionCorrection {

    /** Der Textinhalt der Frage */
    String text;

    /** Die richtige Antwort auf diese Frage */
    String correctAnswer;

    /**
     * Erstellt eine neue QuestionCorrection mit dem angegebenen Fragetext und der richtigen Antwort.
     *
     * @param text der Fragetext
     * @param correctAnswer die richtige Antwort auf diese Frage
     */
    public QuestionCorrection(String text, String correctAnswer) {
        this.text = text;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Überprüft, ob die vom Benutzer bereitgestellte Antwort mit der richtigen Antwort übereinstimmt.
     * Der Vergleich ist nicht case-sensitiv und behandelt Null-Eingaben sicher.
     *
     * @param userAnswer die vom Benutzer bereitgestellte Antwort
     * @return true, wenn die Benutzerantwort mit der richtigen Antwort übereinstimmt (nicht case-sensitiv), false sonst
     */
    public boolean isCorrect(String userAnswer) {
        return userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer);
    }
}
