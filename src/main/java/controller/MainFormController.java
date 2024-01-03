package controller;

import view.MainForm;

import java.util.Objects;

public class MainFormController {
    private static MainForm mainForm;
    private static MainFormController mainFormController;

    public MainFormController() {
        control();
    }

    public static MainFormController getInstance() {
        if (Objects.isNull(mainFormController)) {
            mainForm = new MainForm();
            mainFormController = new MainFormController();
        }
        init();
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

        }
    private static void init() {
        mainForm.getFrame().setVisible(true);
    }
}
