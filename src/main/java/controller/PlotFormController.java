package controller;

import model.user.User;
import model.user.UserRole;
import model.utils.PythonScriptRunner;
import model.utils.Utils;
import view.PlotForm;
import model.utils.UtilsDatabase;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlotFormController {
    private static PlotForm plotForm;
    private static PlotFormController plotFormController;

    public PlotFormController() {
        control();
    }
    public static PlotFormController getInstance() {
        if (Objects.isNull(plotFormController)) {
            plotForm = new PlotForm();
            plotFormController = new PlotFormController();
            fillComboBoxPlayerTeam();
            fillComboBoxXY();
        }
        init();
        return plotFormController;
    }
    public void control() {
        plotForm.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                MainFormController.getInstance();
            }
        });
        plotForm.getButtonBack().addActionListener(e -> onButtonBack());
        plotForm.getComboBoxPlayerTeam().addActionListener(e -> onComboBoxPlayerTeam());
        plotForm.getButtonGenerate().addActionListener(e -> onButtonGenerate());
    }
    private static void init() {
        plotForm.getFrame().setVisible(true);
    }
    private void onComboBoxPlayerTeam() {
        plotForm.getComboBoxX().removeAllItems();
        plotForm.getComboBoxY().removeAllItems();
        fillComboBoxXY();
    }
    private void onButtonGenerate() {
        try {
            if(plotForm.getComboBoxPlayerTeam().getSelectedItem().equals("Player")){
                PythonScriptRunner.runPythonScript("src/main/python/GeneratePlotPlayer.py", List.of(plotForm.getComboBoxX().getSelectedItem(), plotForm.getComboBoxY().getSelectedItem()));
            } else {
                PythonScriptRunner.runPythonScript("src/main/python/GeneratePlotTeam.py", List.of(plotForm.getComboBoxX().getSelectedItem(), plotForm.getComboBoxY().getSelectedItem()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void onButtonBack() {
        if(Utils.PARAMS.get("CURRENT_USER") != null)
            if (((User) Utils.PARAMS.get("CURRENT_USER")).getRole() == UserRole.ADMIN)
                MainFormAdminController.getInstance();
            else
                MainFormUserController.getInstance();
        else
            MainFormController.getInstance();
        plotForm.getFrame().dispose();
    }
    private static void fillComboBoxPlayerTeam() {
        plotForm.getComboBoxPlayerTeam().addItem("Player");
        plotForm.getComboBoxPlayerTeam().addItem("Team");
    }
    private static void fillComboBoxXY() {
        plotForm.getComboBoxX().removeAllItems();
        plotForm.getComboBoxY().removeAllItems();
        if(plotForm.getComboBoxPlayerTeam().getSelectedItem().equals("Player")){
            List<String> playerStats = Arrays.asList("pts", "trb", "ast", "stl", "mp", "pf", "fg", "fga", "fg_pct",
                    "fg3", "fg3a", "fg3_pct", "ft", "fta", "ft_pct", "orb", "drb", "tov", "plus_minus", "blk", "ts_pct", "2p", "2pa", "2p_pct", "efg_pct");
            for(String stat : playerStats){
                plotForm.getComboBoxX().addItem(stat);
                plotForm.getComboBoxY().addItem(stat);
            }
        } else {
            List<String> teamStats = Arrays.asList("pts", "trb", "ast", "stl", "fg", "fga", "fg_pct",
                    "fg3", "fg3a", "fg3_pct", "ft", "fta", "ft_pct", "orb", "drb", "tov", "blk", "ts_pct", "2p", "2pa", "2p_pct", "efg_pct");
            for(String stat : teamStats){
                plotForm.getComboBoxX().addItem(stat);
                plotForm.getComboBoxY().addItem(stat);
            }
        }
    }
}

