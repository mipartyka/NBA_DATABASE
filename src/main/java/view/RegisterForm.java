package view;

import lombok.Getter;

import javax.swing.*;

@Getter
public class RegisterForm {
    private JPanel panel;
    private JTextField textFieldLogin;
    private JPasswordField passwordFieldPassword;
    private JButton buttonRegister;
    private JButton buttonLogIn;
    private JButton buttonBack;
    private JPasswordField passwordFieldRepeatPassword;
    private JFrame frame;

    public RegisterForm() {
        frame = new JFrame("Register");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
