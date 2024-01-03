package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class MainForm {
    private JPanel panel;
    private JTextField textFieldURL;
    private JButton buttonGenerate;
    private JButton buttonGenerateFromDay;
    private JButton buttonStandings;
    private JButton buttonGames;
    private JButton buttonLeagueLeaders;
    private JButton buttonLogIn;
    private JButton buttonRegister;
    private JButton buttonPlot;

    private JFrame frame;

    public MainForm() {
        frame = new JFrame("MainForm");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }
}
