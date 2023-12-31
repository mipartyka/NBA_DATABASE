package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class LogInForm {
    private JPanel panel;
    private JTextField textFieldLogin;
    private JPasswordField passwordFieldPassword;
    private JButton buttonLogIn;
    private JButton buttonRegister;
    private JButton buttonBack;
    private JFrame frame;

    public LogInForm() {
        frame = new JFrame("Log In");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
