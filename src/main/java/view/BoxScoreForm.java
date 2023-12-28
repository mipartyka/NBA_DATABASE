package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class BoxScoreForm {
    private JTable tableBoxScore;
    private JPanel panel;
    private JButton buttonHomeTeam;
    private JButton buttonAwayTeam;
    private JLabel labelDescription;
    private JFrame frame;

    public BoxScoreForm() {
        frame = new JFrame("BoxScore");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
