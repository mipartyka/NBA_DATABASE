package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class MainForm {
    private JPanel panel;
    private JTextField textFieldURL;
    private JButton buttonGenerate;
    private JButton buttonGenerateFromDay;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxYear;
    private JComboBox comboBoxMonth;

    private JFrame frame;

    public MainForm() {
        frame = new JFrame("MainForm");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }
}
