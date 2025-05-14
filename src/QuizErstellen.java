package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse {@code QuizErstellen} verwaltet eine Liste von Fragen und bietet
 * Methoden zum Speichern und Laden der Fragen in/aus einer Datei.
 *
 * Die Fragen werden im Verzeichnis {@code C:/temp/Quiz} gespeichert.
 */
public class QuizErstellen {
    private List<Questions> questionList = new ArrayList<>();

    /**
     * Fügt eine neue Frage zur internen Liste hinzu.
     *
     * @param question Die hinzuzufügende Frage
     */
    public void addQuestion(Questions question) {
        questionList.add(question);
    }

    /**
     * Gibt die Liste aller gespeicherten Fragen zurück.
     *
     * @return Liste von {@code Questions}
     */
    public List<Questions> getQuestions() {
        return questionList;
    }

    /**
     * Speichert alle Fragen in eine Datei im Verzeichnis {@code C:/temp/Quiz}.
     * Jede Frage wird im Anfügemodus gespeichert.
     *
     * @param filename Der Dateiname (z. B. "fragen.txt")
     * @throws IOException Falls beim Schreiben ein Fehler auftritt
     */
    public void saveQuiz(String filename) throws IOException {
        for (Questions frage : questionList) {
            frage.writeToFile(filename);
        }
    }

    /**
     * Lädt alle Fragen aus einer Datei im Verzeichnis {@code C:/temp/Quiz}.
     * Die Datei muss im passenden Format (6 Zeilen pro Frage) vorliegen.
     *
     * @param filename Der Dateiname (z. B. "fragen.txt")
     * @throws IOException Falls beim Lesen ein Fehler auftritt
     */
    public void loadQuiz(String filename) throws IOException {
        questionList.clear();

        String dirPath = "C:/temp/Quiz";
        String fullPath = dirPath + "/" + filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String question = line;
                String[] answers = new String[4];
                for (int i = 0; i < 4; i++) {
                    answers[i] = reader.readLine();
                }
                int correctIndex = Integer.parseInt(reader.readLine());
                Questions q = new Questions(question, answers, correctIndex);
                questionList.add(q);
            }
        }
    }
}
