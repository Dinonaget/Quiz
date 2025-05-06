package src;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class QuizGUI {
    public QuizGUI() {
        JFrame mainFrame = new JFrame("Quiz");

//        private int getCorrectAnswer() {
//            Scanner reader = new Scanner("questions.txt");
//            String line = reader.nextLine();
//
//            JSONObject obj = new JSONObject(line);
//
//            return (int) obj.get("correct");
//        }





        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
    }
}
