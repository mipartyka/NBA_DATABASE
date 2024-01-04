package controller;

import model.utils.PythonScriptRunner;
import model.utils.Utils;
import view.MainFormAdmin;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.Objects;

public class MainFormAdminController {
    private static MainFormAdmin mainFormAdmin;
    private static MainFormAdminController mainFormAdminController;
    private static PythonScriptRunner pythonScriptRunner = new PythonScriptRunner();


    public MainFormAdminController() {
        control();
    }
    public static MainFormAdminController getInstance() {
        if (Objects.isNull(mainFormAdminController)) {
            mainFormAdmin = new MainFormAdmin();
            mainFormAdminController = new MainFormAdminController();
        }
        init();
        return mainFormAdminController;
    }
    public void control() {
        mainFormAdmin.getButtonGames().addActionListener(e -> {
            GamesFormController.getInstance();
            mainFormAdmin.getFrame().dispose();
        });
        mainFormAdmin.getButtonLeagueLeaders().addActionListener(e -> {
            LeagueLeadersFormController.getInstance();
            mainFormAdmin.getFrame().dispose();
        });
        mainFormAdmin.getButtonStandings().addActionListener(e -> {
            StandingsFormController.getInstance();
            mainFormAdmin.getFrame().dispose();
        });
        mainFormAdmin.getButtonLogOut().addActionListener(e -> {
            Utils.PARAMS.remove("CURRENT_USER");
            MainFormController.getInstance();
            mainFormAdmin.getFrame().dispose();
        });
        mainFormAdmin.getButtonPlot().addActionListener(e -> {
            PlotFormController.getInstance();
            mainFormAdmin.getFrame().dispose();
        });
        mainFormAdmin.getButtonAddGames().addActionListener(e -> {
            AddGamesAdminFormController.getInstance();
            mainFormAdmin.getFrame().dispose();
        });
    }
    private static void init() {
        mainFormAdmin.getFrame().setVisible(true);
        setLabelLogin();
    }
    private static void setLabelLogin() {
        mainFormAdmin.getLabelAdmin().setText("Logged in as Admin: " + Utils.PARAMS.get("CURRENT_USER"));
    }
}
