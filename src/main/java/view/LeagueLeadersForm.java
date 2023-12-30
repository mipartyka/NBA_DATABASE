package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class LeagueLeadersForm {
    private JPanel panel;
    private JTable tablePlayers;
    private JComboBox comboBoxFilter;
    private JButton buttonBack;
    private JComboBox comboBoxOrder;
    private JFrame frame;

    public LeagueLeadersForm() {
        frame = new JFrame("League Leaders");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

}
