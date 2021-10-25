package dialog;

import model.AppConstants;
import model.BankSettingsDTO;
import model.Language;
import model.MyConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class SettingsDialog extends ScreenDialog {

    JButton settCancel, settSave;
    private JComboBox<String> dropDown;
    private JTextField dbhost, a, b;
    private JTextField dbuser;
    private JPasswordField dbpassword;
    private JTextField dbdatabase;
    private JTextField dbdriver;
    private JTextField dbport;
    private JTextField dburl;

    public SettingsDialog(JFrame mainFrame, Language language, BankSettingsDTO bankSettingsDTO) {
        super(mainFrame, true);
        setVisible(false);
        setTitle(language.trans("settings"));
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "settings.png"));
        setIconImage(img);

        dbdriver = new JTextField(80);
        dbhost = new JTextField(20);
        dbport = new JTextField(8);
        dbuser = new JTextField(20);
        dbpassword = new JPasswordField(20);
        dbdatabase = new JTextField(20);
        dburl = new JTextField(20);

        dbdriver.setText(bankSettingsDTO.getDbDriver());
        dbhost.setText(bankSettingsDTO.getDbHost());
        dbport.setText(bankSettingsDTO.getDbPort());
        dbuser.setText(bankSettingsDTO.getDbUser());
        dbpassword.setText(bankSettingsDTO.getDbPassword());
        dbdatabase.setText(bankSettingsDTO.getDbDatabase());

        dropDown = buildDropDown();
        dropDown.setSelectedIndex(bankSettingsDTO.getDbIndex());

        // Create the panels
        setLayout(new GridLayout(10, 2));
        add(new JLabel("  " + language.trans("dbtype") + ":", SwingConstants.LEFT));
        add(dropDown);
        add(new JLabel("  " + language.trans("dbdriver") + ":", SwingConstants.LEFT));
        add(dbdriver);
        add(new JLabel("  " + language.trans("dburl") + ":", SwingConstants.LEFT));
        add(dburl);
        JLabel hostLabel = new JLabel("  " + language.trans("host") + ":", SwingConstants.LEFT);
        add(hostLabel);
        hostLabel.setToolTipText("  " + language.trans("standard"));
        add(dbhost);
        JLabel portLabel = new JLabel("  Port: ", SwingConstants.LEFT);
        add(portLabel);
        portLabel.setToolTipText("  " + language.trans("standard"));
        add(dbport);
        add(new JLabel("  " + language.trans("db") + ":", SwingConstants.LEFT));
        add(dbdatabase);
        add(new JLabel("  " + language.trans("user") + ":", SwingConstants.LEFT));
        add(dbuser);
        add(new JLabel("  " + language.trans("passwort") + ":", SwingConstants.LEFT));
        add(dbpassword);
        add(new JLabel("  Sprache:", SwingConstants.LEFT));
        JTextField field = new JTextField(20);
        field.setText(language.getLanguageStr());
        field.setEditable(false);
        add(field);

        settSave = new JButton(language.trans("save"));
        settSave.setActionCommand(AppConstants.SAVE_SETTINGS);
        add(settSave);

        settCancel = new JButton(language.trans("cancel"));
        settCancel.setActionCommand(AppConstants.CANCEL_SETTINGS);
        add(settCancel);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(mainFrame.getX() + 150, mainFrame.getY(), 380, 260);
        dbdriver.requestFocus();
    }

    private JComboBox<String> buildDropDown() {
        JComboBox<String> dropDown = new JComboBox<>();
        Vector<String> dbtyp = new Vector<>();
        dbtyp.add("MySQL");
        dbtyp.add("Oracle");
        dbtyp.add("MSSQL");
        dbtyp.add("PostgreSQL");
        dbtyp.add("Unknown");
        DefaultComboBoxModel<String> comboBoxModel1 = new DefaultComboBoxModel<>(dbtyp);
        dropDown.setModel(comboBoxModel1);
        dropDown.addActionListener(e -> selectDriver());

        return dropDown;
    }

    private void selectDriver() {
        switch (dropDown.getSelectedIndex()) {
            case 0:
                dbdriver.setText("com.mysql.cj.jdbc.Driver");
                dbport.setText("3306");
                dburl.setText("jdbc:mysql://" + dbhost.getText() + ":" + dbport.getText() + "/" + dbdatabase.getText());
                break;
            case 1:
                dbdriver.setText("oracle.jdbc.OracleDriver");
                dbport.setText("1521");
                break;
            case 2:
                dbdriver.setText("mssql.Driver");
                break;
            case 3:
                dbdriver.setText("PostgreSQL");
                dbport.setText("5432");
                break;
            case 4:
                break;
            default:
                dbdriver.setText("Unkown");
                dbport.setText("6666");
                break;
        }
    }

    public BankSettingsDTO saveAsDto() {
        return new BankSettingsDTO(
                dropDown.getSelectedIndex(),
                dbhost.getText(),
                dbdatabase.getText(),
                dbdriver.getText(),
                dbuser.getText(),
                dbport.getText(),
                String.valueOf(dbpassword.getPassword()),
                "en",
                10,
                10
        );
    }

    public JButton getSettCancel() {
        return settCancel;
    }

    public void setSettCancel(JButton settCancel) {
        this.settCancel = settCancel;
    }

    public JButton getSettSave() {
        return settSave;
    }

    public void setSettSave(JButton settSave) {
        this.settSave = settSave;
    }

    public JComboBox<String> getDropDown() {
        return dropDown;
    }
}
