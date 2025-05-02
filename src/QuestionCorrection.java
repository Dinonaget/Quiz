package src;

public class QuestionCorrection {
    String text;
    String correctAnswer;

    public QuestionCorrection(String text, String correctAnswer) {
        this.text = text;
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrect(String userAnswer) {
        return userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer);
    }

}
