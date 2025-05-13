package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestingMain {
    public static void main(String[] args) {
        ArrayList<String> ar = new ArrayList<>();

        ar.add("7");
        ar.add("6");
        ar.add("5");
        ar.add("8");

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
