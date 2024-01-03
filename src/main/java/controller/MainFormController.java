package controller;

import model.utils.PythonScriptRunner;
import model.utils.Utils;
import view.MainForm;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.Objects;

public class MainFormController {
    private static MainForm mainForm;
    private static MainFormController mainFormController;
    private static PythonScriptRunner pythonScriptRunner = new PythonScriptRunner();


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
        mainForm.getButtonGames().addActionListener(e -> {
            GamesFormController.getInstance();
            mainForm.getFrame().dispose();
        });
        mainForm.getButtonLeagueLeaders().addActionListener(e -> {
            LeagueLeadersFormController.getInstance();
            mainForm.getFrame().dispose();
        });
        mainForm.getButtonStandings().addActionListener(e -> {
            StandingsFormController.getInstance();
            mainForm.getFrame().dispose();
        });
        mainForm.getButtonLogIn().addActionListener(e -> {
            LogInFormController.getInstance();
            mainForm.getFrame().dispose();
        });
        mainForm.getButtonRegister().addActionListener(e -> {
            RegisterFormController.getInstance();
            mainForm.getFrame().dispose();
        });
        mainForm.getButtonPlot().addActionListener(e -> {
            PlotFormController.getInstance();
            mainForm.getFrame().dispose();
        });
        mainForm.getButtonGenerate().addActionListener(e -> onButtonGenerate());

        mainForm.getButtonGenerateFromDay().addActionListener(e -> onButtongenerateFromDay());
        mainForm.getTextFieldURL().getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                onChangeTextFields();
            }

            public void removeUpdate(DocumentEvent e) {
                onChangeTextFields();
            }

            public void insertUpdate(DocumentEvent e) {
                onChangeTextFields();
            }
        });

        }

    private void onButtongenerateFromDay() {
        try {
            pythonScriptRunner.runPythonScript("src/main/python/GenerateGameInsertsFromDay.py", List.of(mainForm.getTextFieldURL().getText()));
            mainForm.getTextFieldURL().setText("");
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
    }
}
