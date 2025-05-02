package src;

import java.io.IOException;

public class TestingMain {
    public static void main(String[] args) {
        String[] ar = new String[]{"5", "8", "7", "9"};
        Questions a = new Questions("Wie viele Planeten hat das Sonnensystem?",ar,2);
        System.out.println(a.toString());
        try {
            a.writeToFile("questions.txt");
            System.out.println("Question saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save question: " + e.getMessage());
        }
    }
}
