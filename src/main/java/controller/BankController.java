package controller;

import entity.AccountOld;
import model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BankController implements ActionListener {
    private final BankModel model;

    private final SettingsController settingsController;


    private Database database;
    private CustomerOld kunde;
    private AccountOld account;
    private BankLogic bankLogic;


    private Language lang;

    public BankController(BankModel model, SettingsController settingsController) {
        this.model = model;

        this.settingsController = settingsController;

        // Check for correct Java version
        /*
        if (!MyBankEngine.checkJavaVersion()) {
            return;
        }
         */

        //Business logic
        bankLogic = BankLogic.getInstance();
        lang = model.getLanguage();

        MyBankEngine.oneInstance();
    }

    public SettingsController getSettingsController() {
        return settingsController;
    }

    public boolean connected() {
        return database != null && database.isOnline();
    }

    public String connectedStr() {
        return (connected()) ? lang.trans("connted") : lang.trans("unconnted");
    }

    public boolean isLoggedIn() {
        return kunde != null;
    }

    public int dbLogin(String user, String pass) {
        // Initialisiere dBase, Objekt wird erzeugt
        database = new Database(model.getBankSettingsDTO());
        /*
         * Führt den Verbindungstest durch, falls noch nicht geschen Diese
         * Methode ändert eine Variable, die besagt, ob Zugang möglich. ist
         *
         * Wenn vorher der Verbindungstest manuell durchgeführt wurde, wird er
         * an dieser Stelle übersprungen
         */
        if (!database.isOnline()) {
            database.connectTest();
        }

        // Verbindung zu DB gestört
        if (!connected()) {
            return 1;
        }

        // Ab hier ist die Verbindung i.O.
        // Prüfe Logindaten
        if (database.userConn(user, pass)) {
            kunde = new CustomerOld(user, database, this);
            return 0;
        }

        // User / Pw Kombi nicht ok
        return 2;
    }

    public void dbLogoff() {
        database.close();
        database = null;
        // Kundendaten freisetzen
        kunde = null;
    }

    public void quit() {
        if (database != null) {
            dbLogoff();
        }
        System.exit(0);
    }


    public String k_getFullname() {
        return kunde.getFullname();
    }

    public String k_getArbeit() {
        String datum = kunde.getArbeit();
        String date = lang.trans("dateformat");
        String arbeitfor = "nodate";
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(datum);
            arbeitfor = new SimpleDateFormat(date).format(d);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return arbeitfor;
    }

    public boolean k_setArbeit(String datum) {
        if (k_getArbeit().equals(datum))
            return true;

        String formArbeit;
        try {
            String date = lang.trans("dateformat");
            // String date="dd.MM.yyyy";
            // if(progLang.equals("en"))date="MM/dd/yyyy";
            Date d = new SimpleDateFormat(date).parse(datum);
            formArbeit = new SimpleDateFormat("yyyy-MM-dd").format(d);
        } catch (ParseException e1) {
            return false;
        }
        try {
            if (kunde.setArbeit(formArbeit))
                return true;
        } catch (Exception e) {
            return false;
        }
        // Default: false
        return false;
    }

    public int k_anzahlKonten() {
        return kunde.anzahlKonten();
    }

    public double k_gesamtSaldo() {
        return kunde.getGesamtsaldo();
    }

    public long[] k_getNummern() {
        return kunde.getNummern();
    }

    public String[] k_listeKontos() {
        return kunde.listeKontos();
    }

    // @TODO
    public void ko_createKonto(int typ, String a, String b) {
        double da = 0.0, db = 0.0;
        try {
            da = Double.parseDouble(a);
            db = Double.parseDouble(b);
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ef) {
                ef.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Probleme bei Eingabe der Werte!", "Fehler 199",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // Bedingungen prüfen
        if (kunde.anzahlKonten() == 5 || kunde.countKonto("SPAR") == 1 && typ == 2 || kunde.countKonto("FEST") == 1 && typ == 3) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ef) {
                ef.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Maximale Anzahl Konten dieser Art erreicht!", "Fehler 555",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            account = new AccountOld(database);
            String bub;
            switch (typ) {
                case 1:
                    bub = "GIRO";
                    break;
                case 2:
                    bub = "SPAR";
                    break;
                case 3:
                    bub = "FEST";
                    break;
                default:
                    bub = "unbekannt";
            }
            String bla = bub;
            Double luba = db;
            luba = db;
            /* @TODO */
            /*
            if (konto.kontoCreate(bub, da, db, kunde.getID())) {
                // mtextarea.append("\nKonto mit Nr. "+konto.getNr()+" wurde
                // erstellt!");
                // temp.dispose();
            }
            */
        }
    }

    public long ko_getNr() {
        return account.getNr();
    }

    public void ko_choose(long nr) {
        account = new AccountOld(database);
        account.getData(nr);
    }

    public String ko_kontoinfo() {
        return account.kontoinfo();
    }

    public String ko_umsatz() {
        return account.umsatz();
    }

    public String connTest() {
        if (database == null) {
            database = new Database(model.getBankSettingsDTO());
        }
        return database.connectTest();
    }

    public BankSettingsDTO getBankSettingsDTO() {
        return model.getBankSettingsDTO();
    }

    public void setBankSettingsDTO(BankSettingsDTO bankSettingsDTO) {
        model.setBankSettingsDTO(bankSettingsDTO);
    }


    /**
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println(cmd);

        if (cmd.equals("setdate_action")) {
            boolean isDone = k_setArbeit(model.getWorkingDay());

            if (!isDone) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception ef) {
                    JOptionPane.showMessageDialog(null, lang.trans("wronginput"), "Error 123",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                //Gui gui = (Gui) view.view;
                //gui.statusBarText();
                //gui.closeSecondScreen();
            }

        }
    }

    public boolean einzahlen(double dbetrag) {
        return account.einzahlen(dbetrag);
    }

    public boolean auszahlen(double dbetrag) {
        return account.auszahlen(dbetrag);
    }

    public BankModel getModel() {
        return model;
    }
}