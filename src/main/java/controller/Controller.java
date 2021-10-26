package controller;

import dialog.*;
import model.AppConstants;
import model.BankSettingsDTO;
import model.Language;
import model.Model;
import view.MyBankMenuBar;
import view.SplashScreen;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Controller implements ActionListener {
    private final Model model;
    private final View view;
    private Language language;

    public Controller(Model m, View v) {
        model = m;
        view = v;

        initModel();
        initView();
    }

    /**
     * Just load all settings and get language
     */
    public void initModel() {
        model.setBankSettingsDTO(SettingsController.loadSettings());
        language = new Language(model.getLanguageStr());
    }

    public void initView() {
        // Windows-Look
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Before GUI is built, we need to set these parameters
        view.setLanguage(language);
        view.setLocation(model.getBankSettingsDTO().getPosition().x, model.getBankSettingsDTO().getPosition().y);

        // For better visibility, we outsource code for main menu to another class
        view.setMyBankMenuBar(new MyBankMenuBar(language));

        buildGui();
    }

    private void quitApplication() {
        System.out.println("graceful exit");
        model.disconnect();
        System.exit(0);
    }

    private void buildGui() {
        Thread t1 = new Thread(new SplashScreen(1));
        t1.setPriority(Thread.MIN_PRIORITY);
        Thread t2 = new Thread(view);
        t2.setPriority(Thread.MAX_PRIORITY);

        SwingUtilities.invokeLater(() -> {
            t1.start();
            t2.start();
        });

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void initController() {
        JTextField console = view.getConsoleInput();
        console.setVisible(false);
        view.setConsoleInput(console);

        ArrayList<JMenuItem> menuItems = view.getMyBankMenuBar().getAllItems();

        for (JMenuItem item : menuItems) {
            if (item != null && !(item instanceof JMenu)) {
                item.addActionListener(this);
            }
        }

        // @TODO: Auslesen der Sprachen
        Component[] changeLanguage = view.getMyBankMenuBar().getLanguageSelectionMenu().getMenuComponents();
        // javax.swing.JRadioButtonMenuItem[,0,0,0x0,invalid,alignmentX=0.0,alignmentY=0.0,border=javax.swing.plaf.basic.BasicBorders$MarginBorder@36c88a32,flags=256,maximumSize=,minimumSize=,preferredSize=,defaultIcon=,disabledIcon=,disabledSelectedIcon=,margin=javax.swing.plaf.InsetsUIResource[top=2,left=2,bottom=2,right=2],paintBorder=false,paintFocus=false,pressedIcon=,rolloverEnabled=false,rolloverIcon=,rolloverSelectedIcon=,selectedIcon=,text=Deutsch]
        //JMenuItem item = (JMenuItem) view.getMyBankMenuBar().getMenu(0).getMenuComponents()[0];

        view.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quitApplication();
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String cmd = actionEvent.getActionCommand();
        System.out.println(cmd);

        switch (cmd) {
            case AppConstants.ABOUT:
                new AboutDialog(view);
                break;
            case AppConstants.LOGON:
                LoginDialog loginDialog = new LoginDialog(view, language);
                loginDialog.getConfirmBtn().addActionListener(e -> processLogin(loginDialog.getUsername(), loginDialog.getUserPassword()));
                view.setSecondaryScreen(loginDialog);
                loginDialog.setVisible(true);
                break;
            case AppConstants.CANCEL_SETTINGS:
                view.closeSecondaryScreen();
                break;
            case AppConstants.QUIT:
                quitApplication();
                break;
            case AppConstants.SETTINGS:
                SettingsDialog settingsDialog = new SettingsDialog(view, language, model.getBankSettingsDTO());
                view.setSecondaryScreen(settingsDialog);
                settingsDialog.getSettSave().addActionListener(this);
                settingsDialog.getSettCancel().addActionListener(this);
                settingsDialog.getDropDown().addKeyListener(new KeyListener() {
                    public void keyTyped(KeyEvent e) {
                    }

                    public void keyReleased(KeyEvent e) {
                    }

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            view.closeSecondaryScreen();
                        }
                    }
                });
                settingsDialog.setVisible(true);
                break;
            case AppConstants.SAVE_SETTINGS:
                settingsDialog = (SettingsDialog) view.getSecondaryScreen();
                BankSettingsDTO bankSettingsDTO = settingsDialog.saveAsDto();
                bankSettingsDTO.setPosition(view.getLocation());
                bankSettingsDTO.setApplicationLanguage(language.getLanguageStr());
                if (SettingsController.saveSettings(bankSettingsDTO)) {
                    view.print(language.trans("saveset"));
                }
                view.closeSecondaryScreen();
                break;
            case AppConstants.CONSOLE:
                boolean newShowStatus = !view.getConsoleInput().isVisible();
                if (view.getMyBankToolBar().getConsoleCheckbox() != null) {
                    view.getMyBankToolBar().getConsoleCheckbox().setSelected(newShowStatus);
                }
                view.getConsoleInput().setVisible(newShowStatus);
                view.setSize(view.getWidth(), view.getHeight() + 1);
                view.setSize(view.getWidth(), view.getHeight() - 1);
                break;
            case AppConstants.TB_SELECT_ACCOUNT:
                SelectAccountDialog selectAccountDialog = new SelectAccountDialog(view, language);
                view.setSecondaryScreen(selectAccountDialog);

                break;
            case AppConstants.TB_WORK_DAY:
                WorkingDayDialog workingDayDialog = new WorkingDayDialog(view, language, model.getWorkingDay());
                view.setSecondaryScreen(workingDayDialog);
                workingDayDialog.getApplyButton().addActionListener((ActionEvent e) -> {
                    String newWorkingDay = workingDayDialog.getWorkDate();
                    if (model.saveWorkingDay(newWorkingDay)) {
                        view.updateStatusBar(model.getUser());
                        view.closeSecondaryScreen();
                        view.print(language.trans("set_work_apply"));
                    } else {
                        view.print(language.trans("set_work_problem"));
                    }
                });
                workingDayDialog.getDateTextField().addKeyListener(new KeyListener() {
                    public void keyTyped(KeyEvent e) {
                    }

                    public void keyReleased(KeyEvent e) {
                    }

                    public void keyPressed(KeyEvent e) {
                        int key = e.getKeyCode();
                        if (key == KeyEvent.VK_ESCAPE) {
                            view.closeSecondaryScreen();
                        }
                        if (key == KeyEvent.VK_ENTER) {
                            workingDayDialog.getApplyButton().doClick();
                        }
                    }
                });
                workingDayDialog.setVisible(true);
                break;
        }
    }

    /**
     * @param username     username
     * @param userPassword password
     */
    private void processLogin(String username, String userPassword) {
        int loginResult = model.login(username, userPassword);

        // Connection error
        if (loginResult == AppConstants.CONNECTION_ERROR) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, language.trans("noConn"),
                    "Connection Error", JOptionPane.WARNING_MESSAGE);
        }

        // Credentials invalid
        if (loginResult == AppConstants.WRONG_CREDENTIALS) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ef) {
                return;
            }
            JOptionPane.showMessageDialog(null, language.trans("noaccess"),
                    "Error 404", JOptionPane.ERROR_MESSAGE);
        }

        // No error, login successful
        if (loginResult == AppConstants.LOGIN_SUCCESS) {
            view.closeSecondaryScreen();
            view.updateStatusBar(model.getUser());
            view.loadToolbar();
            registerToolbarActions();
        }
    }

    private void registerToolbarActions() {
        ArrayList<JButton> menuItems = view.getMyBankToolBar().getAllButtons();

        for (JButton item : menuItems) {
            if (item != null) {
                item.addActionListener(this);
            }
        }

        // Toggling console or not
        JCheckBox consoleCheckbox = view.getMyBankToolBar().getConsoleCheckbox();
        consoleCheckbox.addItemListener(e -> {
                    JCheckBox cb = (JCheckBox) e.getItemSelectable();
                    // Fehler in der GUI, die einzige Methode es hinzukriegen
                    if (cb.isSelected()) {
                        view.setSize(view.getWidth(), view.getHeight() + 1);
                        view.setSize(view.getWidth(), view.getHeight() - 1);
                        view.getConsoleInput().setVisible(true);
                        view.getConsoleInput().requestFocus();
                    } else {
                        view.getConsoleInput().setVisible(false);
                        System.out.println("Hide Console");
                        // Nonsens ich weiß, aber bisher die einzige Möglichkeit
                        view.setSize(view.getWidth(), view.getHeight() + 1);
                        view.setSize(view.getWidth(), view.getHeight() - 1);
                    }
                }
        );

        view.getConsoleInput().addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    view.getConsoleInput().setVisible(false);
                    view.setSize(view.getWidth(), view.getHeight() + 1);
                    view.setSize(view.getWidth(), view.getHeight() - 1);
                    consoleCheckbox.setSelected(false);
                    //System.out.println(view.getConsoleInput().getText());
//                    execute(console.getText());
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println(view.getConsoleInput().getText());
//                    execute(console.getText());
                }
            }
        });
    }

    /**
     * @deprecated
     */
    public void tempLogin() {
        processLogin("waldi", "w");
    }
}
