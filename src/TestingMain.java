package src;

import java.io.IOException;

public class TestingMain {
    public static void main(String[] args) {
        String[] ar = new String[]{"3", "4", "5", "6"};
        Questions a = new Questions("Was ist 2+2?",ar,1);
        System.out.println(a.toString());
        try {
            a.writeToFile("questions.txt");
            System.out.println("Question saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save question: " + e.getMessage());
        }
    }
}
