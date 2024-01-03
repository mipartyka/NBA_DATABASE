package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class AddGamesAdminForm {
    private JPanel panel;
    private JTextField textFieldIDURL;
    private JButton buttonAddIDURL;
    private JComboBox comboBoxMonth;
    private JButton buttonAddDate;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxYear;
    private JComboBox comboBoxWeek;
    private JButton buttonAddWeek;
    private JButton buttonBack;
    private JFrame frame;

    public AddGamesAdminForm() {
        frame = new JFrame("Add Games");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
