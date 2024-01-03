package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class PlotForm {
    private JPanel panel;
    private JComboBox comboBoxX;
    private JComboBox comboBoxY;
    private JComboBox comboBoxPlayerTeam;
    private JButton buttonBack;
    private JButton buttonGenerate;
    private JFrame frame;

    public PlotForm() {
        frame = new JFrame("Plot");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
