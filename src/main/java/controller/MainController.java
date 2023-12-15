package controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import model.PythonScrapers;
import lombok.Getter;

public class MainController {
    private static MainController mainController;

    private static final PythonScrapers pythonScrapers = new PythonScrapers();


    private MainController() throws IOException {
        control();
    }

    public static MainController getInstance() throws IOException {
        if (Objects.isNull(mainController)) {
            mainController = new MainController();
        }
        return mainController;
    }

    public void control() throws IOException {
        init();
    }


    private static void init() throws IOException {
        MainFormController.getInstance();
    }
}
