package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class StandingsForm {
    private JPanel panel;
    private JTable tableStandings;
    private JFrame frame;

    public StandingsForm() {
        frame = new JFrame("StandingsForm");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }
}
