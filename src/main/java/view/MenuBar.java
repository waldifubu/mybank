package view;

import model.Language;
import model.MyConstants;
import view.Gui;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @deprecated
 */
public class MenuBar extends JMenuBar {

    private Language lang;
    private Gui gui;

    //@TODO: Load all available languages
    public MenuBar(Language lang, Gui gui) {
        super();
        JMenu datei = new JMenu(lang.trans("file"));
        datei.setMnemonic(KeyEvent.VK_D);

        JMenu extras = new JMenu(lang.trans("extras"));
        extras.setMnemonic(KeyEvent.VK_X);

        JMenu hilfe = new JMenu(lang.trans("help"));
        hilfe.setMnemonic(KeyEvent.VK_H);

        // Untermenüelemente erzeugen
        JMenuItem anmelden = new JMenuItem(lang.trans("login"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "logon.png")));
        anmelden.setMnemonic(KeyEvent.VK_A);
        anmelden.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK));
        anmelden.setActionCommand("anmelden");
        anmelden.addActionListener(e -> gui.logonScreen());

        JMenuItem abmelden = new JMenuItem(lang.trans("logoff"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "logoff.png")));
        abmelden.setMnemonic(KeyEvent.VK_M);
        abmelden.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK));
        abmelden.setActionCommand("abmelden");
        abmelden.addActionListener(e -> gui.logoff());

        JMenuItem beenden = new JMenuItem(lang.trans("quit"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "quit.png")));
        beenden.setMnemonic(KeyEvent.VK_B);
        beenden.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_DOWN_MASK));
        beenden.setActionCommand("beenden");
        beenden.addActionListener(gui);

        JMenuItem settings = new JMenuItem(lang.trans("settings"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "settings.png")));
        settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK));
        settings.setActionCommand("settings");
        settings.addActionListener(e -> gui.settingsScreen(true));

        JMenuItem conntest = new JMenuItem(lang.trans("conntest"), new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "conntest.png")));
        conntest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK));
        conntest.addActionListener(e -> gui.connectionTestView());

        JMenu langChange = new JMenu("Language (needs restart)");
        langChange.setIcon(new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "world.png")));

        ButtonGroup glanguage = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem1 = new JRadioButtonMenuItem("Deutsch");
        rbMenuItem1.setActionCommand("lang");
        rbMenuItem1.addActionListener(gui);
        JRadioButtonMenuItem rbMenuItem2 = new JRadioButtonMenuItem("English");
        rbMenuItem2.setActionCommand("lang");
        rbMenuItem2.addActionListener(gui);
        JRadioButtonMenuItem rbMenuItem3 = new JRadioButtonMenuItem("Türkiye");
        rbMenuItem3.addActionListener(gui);
        rbMenuItem3.setActionCommand("lang");

        if (lang.getLanguageStr().equals("de")) rbMenuItem1.setSelected(true);
        if (lang.getLanguageStr().equals("en")) rbMenuItem2.setSelected(true);
        if (lang.getLanguageStr().equals("tr")) rbMenuItem3.setSelected(true);
        glanguage.add(rbMenuItem1);
        glanguage.add(rbMenuItem2);
        glanguage.add(rbMenuItem3);
        langChange.add(rbMenuItem1);
        langChange.add(rbMenuItem2);
        langChange.add(rbMenuItem3);

        JMenuItem faq = new JMenuItem("F.A.Q.", new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "faq.png")));
        faq.addActionListener(gui);
        JMenuItem about = new JMenuItem("About", new ImageIcon(getClass().getResource(MyConstants.getImagePath() + "info2.png")));
        about.addActionListener(e -> gui.aboutScreen());

        // Menüelemente hinzufügen
        add(datei);
        add(extras);
        add(hilfe);

        // Untermenüelemente hinzufügen
        datei.add(anmelden);
        datei.add(abmelden);
        datei.add(beenden);
        extras.add(settings);
        extras.add(conntest);
        extras.addSeparator();
        extras.add(langChange);
        hilfe.add(faq);
        hilfe.add(about);
    }

    /**
     * @deprecated
     * Remove in future
     */
    public JMenuBar showMenu() {
        // Menuleiste erzeugen
        JMenuBar jMenuBar = new JMenuBar();

        // Menuleiste 1. Elemente
        JMenu datei = new JMenu(lang.trans("file"));
        datei.setMnemonic(KeyEvent.VK_D);

        JMenu extras = new JMenu(lang.trans("extras"));
        extras.setMnemonic(KeyEvent.VK_X);

        JMenu hilfe = new JMenu(lang.trans("help"));
        hilfe.setMnemonic(KeyEvent.VK_H);

        // Untermenüelemente erzeugen
        JMenuItem anmelden = new JMenuItem(lang.trans("login"), new ImageIcon(getClass().getResource("/images/logon.png")));
        anmelden.setMnemonic(KeyEvent.VK_A);
        anmelden.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK));
        anmelden.setActionCommand("anmelden");
        anmelden.addActionListener(gui);

        JMenuItem abmelden = new JMenuItem(lang.trans("logoff"), new ImageIcon(getClass().getResource("/images/logoff.png")));
        abmelden.setMnemonic(KeyEvent.VK_M);
        abmelden.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK));
        abmelden.setActionCommand("abmelden");
        abmelden.addActionListener(gui);

        JMenuItem beenden = new JMenuItem(lang.trans("quit"), new ImageIcon(getClass().getResource("/images/quit.png")));
        beenden.setMnemonic(KeyEvent.VK_B);
        beenden.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_DOWN_MASK));
        beenden.setActionCommand("beenden");
        beenden.addActionListener(gui);

        JMenuItem settings = new JMenuItem(lang.trans("settings"), new ImageIcon(getClass().getResource("/images/settings.png")));
        settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK));
        settings.setActionCommand("settings");
        settings.addActionListener(e -> gui.settingsScreen(true));

        JMenuItem conntest = new JMenuItem(lang.trans("conntest"), new ImageIcon(getClass().getResource("/images/conntest.png")));
        conntest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK));
        conntest.setActionCommand("conntest");
        conntest.addActionListener(e -> gui.connectionTestView());

        JMenu langChange = new JMenu("Language (needs restart)");
        langChange.setIcon(new ImageIcon(getClass().getResource("/images/world.png")));
        langChange.setMnemonic(KeyEvent.VK_L);

        ButtonGroup glanguage = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem1 = new JRadioButtonMenuItem("Deutsch");
        rbMenuItem1.setActionCommand("lang");
        rbMenuItem1.addActionListener(gui);
        JRadioButtonMenuItem rbMenuItem2 = new JRadioButtonMenuItem("English");
        rbMenuItem2.setActionCommand("lang");
        rbMenuItem2.addActionListener(gui);
        JRadioButtonMenuItem rbMenuItem3 = new JRadioButtonMenuItem("Türkiye");
        rbMenuItem3.addActionListener(gui);
        rbMenuItem3.setActionCommand("lang");

        if (lang.getLanguageStr().equals("de")) rbMenuItem1.setSelected(true);
        if (lang.getLanguageStr().equals("en")) rbMenuItem2.setSelected(true);
        if (lang.getLanguageStr().equals("tr")) rbMenuItem3.setSelected(true);
        glanguage.add(rbMenuItem1);
        glanguage.add(rbMenuItem2);
        glanguage.add(rbMenuItem3);
        langChange.add(rbMenuItem1);
        langChange.add(rbMenuItem2);
        langChange.add(rbMenuItem3);

        JMenuItem faq = new JMenuItem("F.A.Q.", new ImageIcon(getClass().getResource("/images/faq.png")));
        faq.addActionListener(gui);
        JMenuItem about = new JMenuItem("About", new ImageIcon(getClass().getResource("/images/info2.png")));
        about.setActionCommand("about");
        about.addActionListener(gui);

        // Menüelemente hinzufügen
        jMenuBar.add(datei);
        jMenuBar.add(extras);
        jMenuBar.add(hilfe);

        // Untermenüelemente hinzufügen
        datei.add(anmelden);
        datei.add(abmelden);
        datei.add(beenden);
        extras.add(settings);
        extras.add(conntest);
        extras.addSeparator();
        extras.add(langChange);
        hilfe.add(faq);
        hilfe.add(about);

        return jMenuBar;
    }
}
