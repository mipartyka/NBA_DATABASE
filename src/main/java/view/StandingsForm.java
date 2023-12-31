package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class StandingsForm {
    private JPanel panel;
    private JTable tableStandings;
    private JComboBox comboBoxStandings;
    private JComboBox comboBoxDetailed;
    private JButton buttonBack;
    private JFrame frame;

    public StandingsForm() {
        frame = new JFrame("StandingsForm");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }
}
