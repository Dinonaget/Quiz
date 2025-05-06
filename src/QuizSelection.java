package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class QuizSelection extends JFrame {
    public QuizSelection() {
        JFrame frame = new JFrame("Quiz Selection");



        // Create main JFrame.

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        // Create JPanel with GridBagLayout.
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel label = new JLabel("Quiz Selection");
        label.setFont(new Font("Arial", Font.BOLD, 15));

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        label.setBorder(border);

        /*
         * Save slot 1 for a Quiz
         */
        JButton quizButton = new JButton("Save slot 1");
        quizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuizGUI quizGUI = new QuizGUI();
            }
        });
        quizButton.setToolTipText("Save a Quiz");

        /*
         * Save slot 2 for a Quiz
         */
        JButton quizButton2 = new JButton("Saves slot 2");
        quizButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuizGUI quizGUI = new QuizGUI();
            }
        });
        quizButton2.setToolTipText("Save a Quiz");

        /*
         * Save slot 3 for a Quiz
         */
        JButton quizButton3= new JButton("Save slot 3");
        quizButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuizGUI quizGUI = new QuizGUI();
            }
        });
        quizButton3.setToolTipText("Save a Quiz");

        /*
         * Save slot 4 for a Quiz
         */
        JButton quizButton4 = new JButton("Save slot 4");
        quizButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuizGUI quizGUI = new QuizGUI();
            }
        });
        quizButton4.setToolTipText("Save a Quiz");




        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(label);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(quizButton, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(quizButton2, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(quizButton3, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(quizButton4, gbc);

        frame.add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private QuizSelection getQuizSelection() {
        return this;
    }

}
