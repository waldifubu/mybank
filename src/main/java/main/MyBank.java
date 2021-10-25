package main;

import controller.Controller;
import model.Model;
import view.View;

/**
 * DriverClass
 */
public class MyBank {

    public static void main(String[] args) {
        Model m = new Model();
        View v = new View();

        Controller controller = new Controller(m, v);
        controller.initController();

        controller.tempLogin();

/*
        // Responsible for load from and write to settingsfile, not more
        SettingsController settingsController = new SettingsController();

        // Whole settings data are saved in model
        // Database connection through model
        BankModel model = new BankModel();

        model.setBankSettingsDTO(settingsController.readSettings());

        // Get from model the needed translation details
        Language language = new Language(model.language);

        // Abstract kind of view
        BankView view = new BankView("desktop", language);
        //BankView view = new BankView("console", language);

        // The brain of whole application
        BankController controller = new BankController(model, view, settingsController);

        /* register controller as listener
        view.registerListener(controller);
        view.buidlView();

        /*
        if (args.length == 0) {
            if (args[0].equals("desktop")) {
                new MyBank("desktop");
            }
        } else {
            new MyBank("console");
        }
         */
    }
}
