package controller;

import model.DateEnum;
import model.PythonScriptRunner;
import view.MainForm;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class MainFormController {
    private static MainForm mainForm;
    private static MainFormController mainFormController;
    private static PythonScriptRunner pythonScriptRunner = new PythonScriptRunner();
    private static final LocalDateTime localDateTime = LocalDateTime.now();


    public MainFormController() {
        control();
    }

    public static MainFormController getInstance() {
        if (Objects.isNull(mainFormController)) {
            mainForm = new MainForm();
            mainFormController = new MainFormController();
        }
        init();
        mainForm.getButtonGenerateFromDay().setEnabled(false);
        mainForm.getButtonGenerate().setEnabled(false);
        return mainFormController;
    }

    public void control() {
        mainForm.getButtonGenerate().addActionListener(e -> onButtonGenerate());
        mainForm.getButtonGenerateFromDay().addActionListener(e -> onButtongenerateFromDay());
        mainForm.getTextFieldURL().addActionListener(e -> onChangeTextFields());
        mainForm.getComboBoxYear().addActionListener(e -> onChangeComboBoxYear());
        mainForm.getComboBoxMonth().addActionListener(e -> onChangeComboBoxMonth());
        mainForm.getComboBoxDay().addActionListener(e -> onChangeComboBoxDay());

        }

    private void onChangeComboBoxDay() {

    }

    private void onChangeComboBoxMonth() {
        DateEnum dateEnum = (DateEnum) mainForm.getComboBoxMonth().getSelectedItem();
        mainForm.getComboBoxDay().removeAllItems();
        for (int i = 1; i <= dateEnum.getDays(); i++)
            mainForm.getComboBoxDay().addItem(i);
        mainForm.getComboBoxDay().setSelectedIndex(0);
    }

    private void onChangeComboBoxYear() {
        mainForm.getComboBoxMonth().setSelectedIndex(0);
        mainForm.getComboBoxDay().setSelectedIndex(0);
    }

    private void onButtongenerateFromDay() {
        try {
            pythonScriptRunner.runPythonScript("src/main/python/GenerateGameInsertsFromDay.py", List.of(mainForm.getComboBoxMonth(), mainForm.getComboBoxDay(), mainForm.getComboBoxYear()));
            mainForm.getComboBoxDay().setSelectedIndex(0);
            mainForm.getComboBoxMonth().setSelectedIndex(0);
            mainForm.getComboBoxYear().setSelectedIndex(0);
            mainForm.getButtonGenerateFromDay().setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onChangeTextFields() {
        mainForm.getButtonGenerate().setEnabled(!mainForm.getTextFieldURL().getText().isEmpty() && mainForm.getTextFieldURL().getText().contains("https://www.basketball-reference.com/boxscores/") && !mainForm.getTextFieldURL().getText().contains("month"));
        mainForm.getButtonGenerateFromDay().setEnabled(!mainForm.getTextFieldURL().getText().isEmpty() && mainForm.getTextFieldURL().getText().contains("https://www.basketball-reference.com/boxscores/"));
    }

    private void onButtonGenerate() {
        try {
            pythonScriptRunner.runPythonScript("src/main/python/GenerateGameInserts.py", List.of(mainForm.getTextFieldURL().getText()));
            mainForm.getTextFieldURL().setText("");
            mainForm.getButtonGenerate().setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init() {
        mainForm.getFrame().setVisible(true);
        mainForm.getComboBoxYear().addItem("2023");
        mainForm.getComboBoxYear().addItem("2024");
        mainForm.getComboBoxYear().setSelectedItem(localDateTime.getYear());
        for (DateEnum dateEnum : DateEnum.values()) {
            mainForm.getComboBoxMonth().addItem(dateEnum.name());
            if(dateEnum.getMonthToday().equals(dateEnum.getMonthToday())) {
                mainForm.getComboBoxMonth().setSelectedIndex(dateEnum.getMonthToday());
                mainForm.getComboBoxDay().setSelectedIndex(dateEnum.getToday());
            }
        }
        
    }
}
