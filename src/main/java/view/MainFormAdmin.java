package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class MainFormAdmin {
    private JPanel panel1;
    private JButton buttonGames;
    private JButton buttonLeagueLeaders;
    private JButton buttonPlot;
    private JButton buttonStandings;
    private JButton buttonAddGames;
    private JLabel labelAdmin;
    private JButton buttonLogOut;
    private JFrame frame;

    public MainFormAdmin() {
        frame = new JFrame("MainFormAdmin");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
