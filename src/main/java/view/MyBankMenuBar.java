package view;

import model.AppConstants;
import model.Language;
import model.MyConstants;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class MyBankMenuBar extends JMenuBar {

    private JMenu fileMenu;
    private JMenu extrasMenu;
    private JMenu helpMenu;
    private JMenu languageSelectionMenu;

    public MyBankMenuBar(Language lang, View view) {
        super();
        fileMenu = new JMenu(lang.trans("file"));
        fileMenu.setMnemonic(KeyEvent.VK_D);

        extrasMenu = new JMenu(lang.trans("extras"));
        extrasMenu.setMnemonic(KeyEvent.VK_X);

        helpMenu = new JMenu(lang.trans("help"));
        helpMenu.setMnemonic(KeyEvent.VK_H);

        languageSelectionMenu = new JMenu("Language");
        languageSelectionMenu.setToolTipText("needs restart");
        languageSelectionMenu.setIcon(new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "world.png")));

        // Untermenüelemente erzeugen
        JMenuItem anmelden = new JMenuItem(lang.trans("login"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "logon.png")));
        anmelden.setMnemonic(KeyEvent.VK_A);
        anmelden.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK));
        anmelden.setActionCommand(AppConstants.LOGON);

        JMenuItem abmelden = new JMenuItem(lang.trans("logoff"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "logoff.png")));
        abmelden.setMnemonic(KeyEvent.VK_M);
        abmelden.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK));
        abmelden.setActionCommand(AppConstants.LOGOFF);

        JMenuItem quit = new JMenuItem(lang.trans("quit"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "quit.png")));
        quit.setMnemonic(KeyEvent.VK_B);
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_DOWN_MASK));
        quit.setActionCommand(AppConstants.QUIT);

        JMenuItem settings = new JMenuItem(lang.trans("settings"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "settings.png")));
        settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK));
        settings.setActionCommand("settings");
        settings.setActionCommand(AppConstants.SETTINGS);

        JMenuItem conntest = new JMenuItem(lang.trans("conntest"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "conntest.png")));
        conntest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK));
        conntest.setActionCommand(AppConstants.CONN_TEST);
//        conntest.addActionListener(e -> gui.connectionTestView());

        JMenuItem console = new JMenuItem(lang.trans("console"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "console.png")));
        console.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK));
        console.setActionCommand(AppConstants.CONSOLE);

        ButtonGroup glanguage = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem1 = new JRadioButtonMenuItem("Deutsch");
        rbMenuItem1.setActionCommand("lang");
//        rbMenuItem1.addActionListener(gui);
        JRadioButtonMenuItem rbMenuItem2 = new JRadioButtonMenuItem("English");
        rbMenuItem2.setActionCommand("lang");
//        rbMenuItem2.addActionListener(gui);
        JRadioButtonMenuItem rbMenuItem3 = new JRadioButtonMenuItem("Türkiye");
//        rbMenuItem3.addActionListener(gui);
        rbMenuItem3.setActionCommand("lang");

        if (lang.getLanguageStr().equals("de")) rbMenuItem1.setSelected(true);
        if (lang.getLanguageStr().equals("en")) rbMenuItem2.setSelected(true);
        if (lang.getLanguageStr().equals("tr")) rbMenuItem3.setSelected(true);
        glanguage.add(rbMenuItem1);
        glanguage.add(rbMenuItem2);
        glanguage.add(rbMenuItem3);
        languageSelectionMenu.add(rbMenuItem1);
        languageSelectionMenu.add(rbMenuItem2);
        languageSelectionMenu.add(rbMenuItem3);

        JMenuItem faq = new JMenuItem("F.A.Q.", new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "faq.png")));
//        faq.addActionListener(gui);
        JMenuItem about = new JMenuItem("About", new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "info2.png")));
        about.setActionCommand(AppConstants.ABOUT);

        // Menüelemente hinzufügen
        add(fileMenu);
        add(extrasMenu);
        add(helpMenu);

        // Untermenüelemente hinzufügen
        fileMenu.add(anmelden);
        fileMenu.add(abmelden);
        fileMenu.add(quit);

        extrasMenu.add(settings);
        extrasMenu.add(conntest);
        extrasMenu.add(console);
        extrasMenu.addSeparator();
        extrasMenu.add(languageSelectionMenu);

        helpMenu.add(faq);
        helpMenu.add(about);
    }

    public JMenu getFileMenu() {
        return fileMenu;
    }

    public void setFileMenu(JMenu fileMenu) {
        this.fileMenu = fileMenu;
    }

    public JMenu getExtrasMenu() {
        return extrasMenu;
    }

    public void setExtrasMenu(JMenu extrasMenu) {
        this.extrasMenu = extrasMenu;
    }

    @Override
    public JMenu getHelpMenu() {
        return helpMenu;
    }

    public void setHelpMenu(JMenu help) {
        this.helpMenu = help;
    }

    /**
     * @return ArrayList<JMenuItem>
     */
    public ArrayList<JMenuItem> getAllItems() {
        ArrayList<JMenuItem> listComponents = new ArrayList<>();
        IntStream.range(0, getMenuCount()).mapToObj(this::getMenu).forEach(jMenu -> IntStream.range(0, jMenu.getItemCount()).mapToObj(jMenu::getItem).forEach(listComponents::add));
        return listComponents;
    }

    public JMenu getLanguageSelectionMenu() {
        return languageSelectionMenu;
    }

    public void setLanguageSelectionMenu(JMenu languageSelectionMenu) {
        this.languageSelectionMenu = languageSelectionMenu;
    }
}
