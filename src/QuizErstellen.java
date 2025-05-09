package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuizErstellen {
    private String dateiname;
    private List<Questions> questionList = new ArrayList<>();

    public QuizErstellen(String dateiname) {
        this.dateiname = dateiname;
    }

    public void addQuestion(Questions question) {
        questionList.add(question);
    }

    public List<Questions> getQuestions() {
        return questionList;
    }

    public void saveQuiz() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dateiname))) {
            for (Questions frage : questionList) {
                writer.write(frage.formatToLine());
                writer.newLine();
            }
        }
    }

    public void load() throws IOException {
        questionList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(dateiname))) {
            String line;
            while ((line = reader.readLine()) != null) {
                questionList.add(Questions.fromLine(line));
            }
        }
    }
}
