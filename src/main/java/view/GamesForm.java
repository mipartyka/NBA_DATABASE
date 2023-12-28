package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class GamesForm {
    private JPanel panel;
    private JList listGames;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxYear;
    private JComboBox comboBoxMonth;
    private JButton buttonSearch;
    private JFrame frame;

    public GamesForm() {
        frame = new JFrame("Games");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
