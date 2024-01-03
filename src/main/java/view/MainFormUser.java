package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class MainFormUser {
    private JPanel panel;
    private JButton buttonGames;
    private JButton buttonStandings;
    private JButton buttonLogOut;
    private JButton buttonLeagueLeaders;
    private JLabel labelLogin;
    private JButton buttonPlot;
    private JFrame frame;

    public MainFormUser() {
        frame = new JFrame("Main Form");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
