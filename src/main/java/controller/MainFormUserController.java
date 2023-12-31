package controller;

import model.utils.PythonScriptRunner;
import model.utils.Utils;
import view.MainFormUser;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.Objects;

public class MainFormUserController {
    private static MainFormUser mainFormUser;
    private static MainFormUserController mainFormUserController;
    private static PythonScriptRunner pythonScriptRunner = new PythonScriptRunner();


    public MainFormUserController() {
        control();
    }
    public static MainFormUserController getInstance() {
        if (Objects.isNull(mainFormUserController)) {
            mainFormUser = new MainFormUser();
            mainFormUserController = new MainFormUserController();
        }
        init();
        return mainFormUserController;
    }
    public void control() {
        mainFormUser.getButtonGames().addActionListener(e -> {
            GamesFormController.getInstance();
            mainFormUser.getFrame().dispose();
        });
        mainFormUser.getButtonLeagueLeaders().addActionListener(e -> {
            LeagueLeadersFormController.getInstance();
            mainFormUser.getFrame().dispose();
        });
        mainFormUser.getButtonStandings().addActionListener(e -> {
            StandingsFormController.getInstance();
            mainFormUser.getFrame().dispose();
        });
        mainFormUser.getButtonLogOut().addActionListener(e -> {
            Utils.PARAMS.remove("CURRENT_USER");
            MainFormController.getInstance();
            mainFormUser.getFrame().dispose();
        });
    }
    private static void init() {
        mainFormUser.getFrame().setVisible(true);
        setLabelLogin();
    }
    private static void setLabelLogin() {
        mainFormUser.getLabelLogin().setText("Logged in as: " + Utils.PARAMS.get("CURRENT_USER"));
    }
}
