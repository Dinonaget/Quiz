package src;

public class Questioins {
    private String question;
    private String[] answers = new String[4];
    private int correctIndex;
    public Questioins(String question, String[] answers, int correctIndex){
        this.question = question;
        this.answers = answers;
        this.correctIndex = correctIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
}
