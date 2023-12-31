package controller;

import model.utils.Utils;
import view.RegisterForm;
import model.utils.UtilsDatabase;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RegisterFormController {
    private static RegisterForm registerForm;
    private static RegisterFormController registerFormController;

    public RegisterFormController() {
        control();
    }

    public static RegisterFormController getInstance() {
        if (Objects.isNull(registerFormController)) {
            registerForm = new RegisterForm();
            registerFormController = new RegisterFormController();
        }
        init();
        return registerFormController;
    }

    public void control() {
        registerForm.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                MainFormController.getInstance();
            }
        });
        registerForm.getButtonLogIn().addActionListener(e -> onButtonLogIn());
        registerForm.getButtonBack().addActionListener(e -> onButtonBack());
        registerForm.getButtonRegister().addActionListener(e -> onButtonRegister());
        registerForm.getPasswordFieldPassword().addActionListener(e -> checkIfPasswordsMatch());
        registerForm.getPasswordFieldRepeatPassword().addActionListener(e -> checkIfPasswordsMatch());
    }
    private static void init() {
        registerForm.getFrame().setVisible(true);
    }

    private void onButtonRegister() {
        try {
            if(UtilsDatabase.runSqlFunction("get_user_by_login", List.of(registerForm.getTextFieldLogin().getText())).next()){
                Utils.displayOptionPane("User already exists", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                UtilsDatabase.runSqlFunction("add_user", List.of(registerForm.getTextFieldLogin().getText(), String.valueOf(registerForm.getPasswordFieldPassword().getPassword()), "USER"));
                Utils.displayOptionPane("Registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                MainFormController.getInstance();
                registerForm.getFrame().dispose();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        registerForm.getTextFieldLogin().setText("");
        registerForm.getPasswordFieldPassword().setText("");
    }

    private void onButtonLogIn() {
        LogInFormController.getInstance();
        registerForm.getFrame().dispose();
    }

    private void onButtonBack() {
        MainFormController.getInstance();
        registerForm.getFrame().dispose();
    }
    private static void checkIfPasswordsMatch() {
        if (registerForm.getPasswordFieldPassword().getPassword().length == 0 || registerForm.getPasswordFieldRepeatPassword().getPassword().length == 0) {
            registerForm.getButtonRegister().setEnabled(false);
            return;
        }
        if (Arrays.equals(registerForm.getPasswordFieldPassword().getPassword(), registerForm.getPasswordFieldRepeatPassword().getPassword())) {
            registerForm.getButtonRegister().setEnabled(true);
        } else {
            registerForm.getButtonRegister().setEnabled(false);
        }
    }
}

