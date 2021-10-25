package view;

import model.BankSettingsDTO;
import model.Language;
import model.MyConstants;
import dialog.AboutDialog;
import dialog.WorkingDayDialog;
import util.EingabeCheck;
import controller.BankController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Vector;

/** @deprecated */
public class Gui implements Runnable, ActionListener {
    //  Primary class
    private BankController controller;

    //	main frame and temporary screen
    public JFrame mainFrame;
    public JDialog secondaryScreen;

    private JTextField console;

    // if logged on, active
    private JToolBar toolbar;
    private JLabel status;

    // main textare to show all information
    public JTextArea mtextarea;

    private Language language;

    //	Für settings
    public JComboBox<String> dropDown;
    private JTextField dbdriver;
    public JTextField dbhost, a, b;
    private JTextField dbport;
    public JTextField dbuser;
    public JPasswordField dbpassword;
    public JTextField dbdatabase;

    //  Wurde Konto gewählt?
    private boolean kontoChosen;

    //  Sprachenauswahl
    private ButtonGroup glanguage;

    //  Logindata
    private JTextField felduser;
    private JPasswordField feldpass;

    private int help;

    public Gui(Language language) {
        UIManager.put("MenuBar.selectionBackground", Color.BLACK);
        UIManager.put("MenuBar.selectionForeground", Color.YELLOW);
        UIManager.put("Menu.selectionBackground", Color.BLUE);
        UIManager.put("Menu.selectionForeground", Color.WHITE);
        UIManager.put("Menu.background", Color.WHITE);
        UIManager.put("Menu.foreground", Color.BLACK);
        UIManager.put("Menu.opaque", false);
        UIManager.put("Panel.background", Color.RED);
        this.language = language;
    }

    /**
     * Thread is called, Frame and GUI will be built
     */
    @Override
    public void run() {
        // Load primary frame
        mainFrame = new JFrame("myBank v1.0");
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "euro.png"));
        mainFrame.setIconImage(img);

        // Windows-Look
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // for better visibility, we outsource code for main menu to another class
        JMenuBar menu = new MenuBar(language, this);

        // Toolbar for bank functions, movable
        toolbar = new JToolBar(language.trans("func"), JToolBar.VERTICAL);
        toolbar.setVisible(false);

        // create big textarea to see all information
        mtextarea = new JTextArea();
        mtextarea.setFont(new Font("Calibri", Font.PLAIN, 16));
        mtextarea.setText(language.trans("welcomever"));
        mtextarea.setEditable(false);
        mtextarea.setLineWrap(true);
        mtextarea.setBorder(BorderFactory.createLoweredBevelBorder());
        mtextarea.setAutoscrolls(true);

        // special border for a nice look
        Border compound = BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createLineBorder(Color.GRAY));

        // for debugging and power users we have a nice input field as a "console"
        console = new JTextField();
        console.setVisible(false);
        console.setBorder(compound);
        console.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    execute(console.getText());
                }
            }
        });

        // Falls zurueck zur ersten Idee ???
        //        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JScrollPane(mtextarea),console);
        //        split.setResizeWeight(1);

        JScrollPane jp = new JScrollPane(mtextarea);
        Container container = mainFrame.getContentPane();

        container.add(jp, BorderLayout.CENTER);
        container.add(console, BorderLayout.SOUTH);
        jp.setWheelScrollingEnabled(true);
        jp.setAutoscrolls(true);

        JPanel panel = new JPanel();
        BorderLayout bord = new BorderLayout();
        panel.setLayout(bord);
        panel.add("North", menu);
        panel.add("West", toolbar);

        // container consists of 2 elements
        panel.add("Center", container);

        // statusbar at the bottom
        status = new JLabel(language.trans("welcome"));
        status.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.add("South", status);

        // last but not least...
        mainFrame.setContentPane(panel);
        mainFrame.pack();
        mainFrame.setBounds(controller.getBankSettingsDTO().getPosition().x, controller.getBankSettingsDTO().getPosition().y, 700, 380);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.toFront();
        mainFrame.setAutoRequestFocus(true);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.quit();
            }
        });

        // Load all settings to fill up all variables, with param. true settingsScreen will be shown
        settingsScreen(false);

        /**
         * @TODO: Auto logon Baustelle!!!!
         */
        felduser = new JTextField(20);
        felduser.setText("waldi");
        feldpass = new JPasswordField(20);
        feldpass.setText("w");
        logon();
    }

    /**
     * Echtes Anzeigen des Bildschirms (TRUE) oder lediglich zuweisen der Einstellungen<br/>
     * zwecks Aktualisierung (FALSE)
     *
     * @param real true false
     */
    public void settingsScreen(boolean real) {
        if (real) {
            secondaryScreen = new JDialog(mainFrame, true);
            secondaryScreen.setTitle(language.trans("settings"));
            Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "settings.png"));
            secondaryScreen.setIconImage(img);
        }

        dropDown = buildDropDown();
        dbdriver = new JTextField(80);
        dbhost = new JTextField(20);
        dbport = new JTextField(8);
        dbuser = new JTextField(20);
        dbpassword = new JPasswordField(20);
        dbdatabase = new JTextField(20);

        dropDown.setSelectedIndex(controller.getBankSettingsDTO().getDbIndex());
        dbdriver.setText(controller.getBankSettingsDTO().getDbDriver());
        dbhost.setText(controller.getBankSettingsDTO().getDbHost());
        dbport.setText(controller.getBankSettingsDTO().getDbPort());
        dbuser.setText(controller.getBankSettingsDTO().getDbUser());
        dbpassword.setText(controller.getBankSettingsDTO().getDbPassword());
        dbdatabase.setText(controller.getBankSettingsDTO().getDbDatabase());

        if (real) {
            // Create the panels
            secondaryScreen.setLayout(new GridLayout(9, 2));
            secondaryScreen.add(new JLabel("  " + language.trans("dbtype") + ":", SwingConstants.LEFT));
            secondaryScreen.add(dropDown);
            secondaryScreen.add(new JLabel("  " + language.trans("dbdriver") + ":", SwingConstants.LEFT));
            secondaryScreen.add(dbdriver);
            JLabel hostLabel = new JLabel("  " + language.trans("host") + ":", SwingConstants.LEFT);
            secondaryScreen.add(hostLabel);
            hostLabel.setToolTipText("  " + language.trans("standard"));
            secondaryScreen.add(dbhost);
            JLabel portLabel = new JLabel("  Port: ", SwingConstants.LEFT);
            secondaryScreen.add(portLabel);
            portLabel.setToolTipText("  " + language.trans("standard"));
            secondaryScreen.add(dbport);
            secondaryScreen.add(new JLabel("  " + language.trans("db") + ":", SwingConstants.LEFT));
            secondaryScreen.add(dbdatabase);
            secondaryScreen.add(new JLabel("  " + language.trans("user") + ":", SwingConstants.LEFT));
            secondaryScreen.add(dbuser);
            secondaryScreen.add(new JLabel("  " + language.trans("passwort") + ":", SwingConstants.LEFT));
            secondaryScreen.add(dbpassword);
            secondaryScreen.add(new JLabel("  Sprache:", SwingConstants.LEFT));
            JTextField field = new JTextField(20);
            field.setText(language.getLanguageStr());
            field.setEditable(false);
            secondaryScreen.add(field);

            JButton sett_abbrechen, sett_speichern;
            sett_speichern = new JButton(language.trans("save"));
            sett_speichern.setActionCommand("sett_speichern");
            secondaryScreen.add(sett_speichern);
            sett_speichern.addActionListener(e -> {
                controller.getSettingsController().saveSettings(saveAsDto());
                mtextarea.append("\n" + language.trans("saveset"));
                closeSecondScreen();
            });

            sett_abbrechen = new JButton(language.trans("cancel"));
            secondaryScreen.add(sett_abbrechen);
            sett_abbrechen.addActionListener(e -> closeSecondScreen());

            secondaryScreen.setResizable(false);
            secondaryScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            secondaryScreen.setBounds(mainFrame.getX() + 50, mainFrame.getY(), 300, 250);
            dbdriver.requestFocus();
            secondaryScreen.setVisible(true);
        }
    }


    public void closeSecondScreen() {
        secondaryScreen.dispose();
    }


    private JComboBox<String> buildDropDown() {
        JComboBox<String> dropDown = new JComboBox<String>();
        Vector<String> dbtyp = new Vector<String>();
        dbtyp.add("MySQL");
        dbtyp.add("Oracle");
        dbtyp.add("MSSQL");
        dbtyp.add("PostgreSQL");
        dbtyp.add("Unbekannt");
        DefaultComboBoxModel<String> comboBoxModel1 = new DefaultComboBoxModel<String>(dbtyp);
        dropDown.setModel(comboBoxModel1);
        dropDown.addActionListener(e -> selectDriver());
        dropDown.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    closeSecondScreen();
                }
            }
        });

        return dropDown;
    }

    /**
     * Anmeldebildschirm
     */
    public void logonScreen() {
        secondaryScreen = new JDialog(mainFrame, true);
        secondaryScreen.setTitle(language.trans("login"));
        secondaryScreen.getContentPane();
        secondaryScreen.setLayout(new GridLayout(3, 2));
        secondaryScreen.add(new JLabel("  " + language.trans("user") + ":", SwingConstants.LEFT));
        felduser = new JTextField(20);
        felduser.requestFocus();

        felduser.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ESCAPE) {
                    closeSecondScreen();
                }
            }
        });

        secondaryScreen.add(felduser);
        secondaryScreen.add(new JLabel("  " + language.trans("passwort") + ":", SwingConstants.LEFT));
        feldpass = new JPasswordField(20);

        secondaryScreen.add(feldpass);
        secondaryScreen.add(new JLabel(" "));
        final JButton bLogin = new JButton(language.trans("connect"));
        secondaryScreen.add(bLogin);
        bLogin.addActionListener(e -> logon());

        feldpass.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    closeSecondScreen();
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    if (!felduser.getText().equals("")) {
                        feldpass.getPassword();
                        bLogin.doClick();
                    }
                    //Fürs Archiv, gleiches Resultat (Wir emulieren den Klick) wie:
                    //actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED, "logon_connect"));
                }
            }
        });
        secondaryScreen.setResizable(false);
        secondaryScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        secondaryScreen.setBounds(mainFrame.getX(), mainFrame.getY(), 220, 100);
        secondaryScreen.setVisible(true);
    }

    /**
     * If logged in, toolbar will be displayed
     */
    private void loadBankToolbar() {
        toolbar.removeAll();
        JButton kauswahl = new JButton(language.trans("tb_choose"));
        kauswahl.setFont(new Font("", Font.BOLD, 12));
        kauswahl.setToolTipText(language.trans("tb_first"));
        kauswahl.addActionListener(e -> auswahlScreen());
        kauswahl.setMnemonic(KeyEvent.VK_1);

        JButton ktnew = new JButton(language.trans("tb_create"));
        ktnew.addActionListener(e -> createAccountScreen());
        ktnew.setMnemonic(KeyEvent.VK_2);

        JButton ktdel = new JButton(language.trans("tb_close"));
        ktdel.setMnemonic(KeyEvent.VK_3);

        JButton einzahlen = new JButton(language.trans("tb_in"));
        einzahlen.addActionListener(e -> paymentScreen("ein"));
        einzahlen.setMnemonic(KeyEvent.VK_4);

        JButton abheben = new JButton(language.trans("tb_out"));
        abheben.addActionListener(e -> paymentScreen("aus"));
        abheben.setMnemonic(KeyEvent.VK_5);

        JButton uberweisung = new JButton(language.trans("tb_transfer"));

        JButton umsatz = new JButton(language.trans("tb_sales"));
        umsatz.addActionListener(e -> salesView());
        umsatz.setMnemonic(KeyEvent.VK_7);

        JButton kinfo = new JButton(language.trans("tb_info"));
        kinfo.addActionListener(e -> accountView());
        kinfo.setMnemonic(KeyEvent.VK_8);

        JButton festgeld = new JButton(language.trans("tb_fix"));
        festgeld.setMnemonic(KeyEvent.VK_F);

        JButton sparbuch = new JButton(language.trans("tb_save"));

        JButton workday = new JButton(language.trans("tb_workday"));
//        workday.addActionListener(e -> workingDayScreen());

        JCheckBox conon = new JCheckBox(" " + language.trans("tb_console"), false);
        conon.setMnemonic(KeyEvent.VK_C);

        // Console ausblenden
        conon.addItemListener(e -> {
            JCheckBox cb = (JCheckBox) e.getItemSelectable();
            // Fehler in der GUI, die einzige Methode es hinzukriegen
            if (cb.isSelected()) {
                console.setVisible(true);
                console.requestFocus();
            } else
                console.setVisible(false);
            // Nonsens ich weiß, aber bisher die einzige Möglichkeit
            mainFrame.setSize(mainFrame.getWidth(), mainFrame.getHeight() + 1);
            mainFrame.setSize(mainFrame.getWidth(), mainFrame.getHeight() - 1);
        });

        toolbar.add(kauswahl);
        toolbar.add(ktnew);
        toolbar.add(ktdel);
        toolbar.addSeparator();
        toolbar.add(einzahlen);
        toolbar.add(abheben);
        toolbar.add(uberweisung);
        toolbar.add(umsatz);
        toolbar.add(kinfo);
        toolbar.addSeparator();
        toolbar.add(festgeld);
        toolbar.addSeparator();
        toolbar.add(sparbuch);
        toolbar.addSeparator();
        toolbar.add(workday);
        toolbar.addSeparator();
        toolbar.add(conon);
        toolbar.setRollover(true);
        toolbar.setToolTipText(language.trans("toolbartip"));
        toolbar.setVisible(true);
    }

    private void accountView() {
        if (kontoChosen) {
            mtextarea.setAutoscrolls(true);
            mtextarea.append(controller.ko_kontoinfo());
            mtextarea.setAutoscrolls(true);
        }
    }

    /**
     * @param direction ein = einzahlen, aus = auszahlen
     */
    public void paymentScreen(final String direction) {
        //Ein Konto wurde bereits gewählt
        if (kontoChosen) {
            secondaryScreen = new JDialog(mainFrame, true);
            secondaryScreen.setTitle(direction.equals("ein") ? language.trans("tb_in") : language.trans("tb_out"));
            secondaryScreen.getContentPane();
            secondaryScreen.setLayout(new GridLayout(2, 2));
            secondaryScreen.add(new JLabel("  " + language.trans("amount") + ":"));

            final JButton act = new JButton(language.trans("apply"));
            act.getAccessibleContext().setAccessibleDescription("" + direction);
            act.addActionListener(e -> {
//                JButton button = (JButton) e.getSource();
                String result = barZahlung(direction.equals("ein") ? 1 : 0, a.getText());
                mtextarea.setAutoscrolls(true);
                mtextarea.append("\n" + result);
                closeSecondScreen();
            });

            a = new JTextField(new EingabeCheck(), null, 3);
            a.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                }

                public void keyReleased(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER && !a.getText().isEmpty()) {
                        act.doClick();
                    }
                    if (key == KeyEvent.VK_ESCAPE) {
                        closeSecondScreen();
                    }
                }
            });
            String partner = direction.equals("ein") ? language.trans("to") : language.trans("from");

            secondaryScreen.add(a);
            secondaryScreen.add(new JLabel("  " + partner + " " + language.trans("knummer") + ": " + controller.ko_getNr()));
            secondaryScreen.add(act);
            secondaryScreen.setResizable(false);
            secondaryScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            secondaryScreen.setBounds(mainFrame.getX(), mainFrame.getY(), 360, 80);
            secondaryScreen.setVisible(true);
        }
    }

    public void salesView() {
        if (kontoChosen) {
            mtextarea.setAutoscrolls(true);
            mtextarea.append(controller.ko_umsatz());
            mtextarea.setAutoscrolls(true);
        }
    }


    /**
     * Auswahl des zu erstellenden Konto
     * <br/>Bildschirmmaske
     */
    public void createAccountScreen() {
        secondaryScreen = new JDialog(mainFrame, true);
        secondaryScreen.setTitle("Kontoart wählen");
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "mybank.ico"));
        secondaryScreen.setIconImage(img);

        //Ich will das Icon haben, dafür muss frame resizable sein
        //mit dem Listener zwinge ich es auf die gewünschte Größe
        secondaryScreen.addComponentListener(new ComponentListener() {
            @Override
            public void componentShown(ComponentEvent arg0) {
            }

            @Override
            public void componentResized(ComponentEvent arg0) {
                secondaryScreen.setBounds(mainFrame.getX(), mainFrame.getY(), 330, 200);
            }

            @Override
            public void componentMoved(ComponentEvent arg0) {
            }

            @Override
            public void componentHidden(ComponentEvent arg0) {
            }
        });

        ImageIcon icon1 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "giro.png")));
        ImageIcon icon2 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "spar.png")));
        ImageIcon icon3 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "fest.png")));

        JPanel comboBoxPane = new JPanel();
        comboBoxPane.setLayout(new BorderLayout());
        final JTextField a1, a2, b1, b2, c1, c2;

        /**
         *  GIRO
         */
        JPanel card1 = new JPanel();
        card1.add(new JLabel("Überzug in Prozent / Jahr"));
        card1.add(a1 = new JTextField("15.0", 4));
        card1.add(new JLabel("     Gebühren je Quartal in Euro:"));
        card1.add(a2 = new JTextField("3.00", 4));
        JLabel bla = new JLabel("Bitte beachten Sie: Eingaben mit . statt , zu versehen");
        bla.setFont(new Font("", Font.BOLD, 12));
        card1.add(bla);


        /**
         *  SPAR
         */
        JPanel card2 = new JPanel();
        card2.add(new JLabel("Bitte den Zinssatz für"));
        card2.add(new JLabel("die Zinsen / Jahr eingeben"));
        card2.add(b1 = new JTextField("1.5", 4));
        //reserviert, OHNE funk!!!
        b2 = new JTextField("1.5", 3);
        b2.getColumns();


        /**
         *  FEST
         */
        JPanel card3 = new JPanel();
        card3.add(new JLabel("Bitte den Zinssatz für"));
        card3.add(new JLabel("die Zinsen / Jahr eingeben"));
        card3.add(c1 = new JTextField("1.5", 4));
        card3.add(new JLabel("Und die Laufzeit in Jahren"));
        card3.add(c2 = new JTextField("4", 4));

        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Girokonto", icon1, card1, "Girokonto");
        tabbedPane.addTab("Sparbuch", icon2, card2, "Sparbuch");
        tabbedPane.addTab("Festgeldkonto", icon3, card3, "Festgeld");

        tabbedPane.setSelectedIndex(0);

        /* Init wert, wenn das Fenster geöffnet wird.
         * Wird später den Index des Tabs zurückgeben
         */
        help = 0;
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                help = tabbedPane.getSelectedIndex();
            }
        });

        //Konto erzeugen
        JButton ok = new JButton("anlegen");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (help) {
                    case 0:
                        a = a1;
                        b = a2;
                        break;
                    case 1:
                        a = b1;
                        b = b2;
                        break;
                    case 2:
                        a = c1;
                        b = c2;
                        break;
                }

                // Parameter: help+1, da ich keine "0" Werte haben möchte
                help++;
                if (a.getText().contains(",")) {
                    a.setText(a.getText().replace(',', '.'));
                }
                if (b.getText().contains(",")) {
                    b.setText(b.getText().replace(',', '.'));
                }
                System.out.println(a.getText() + " " + b.getText());

                controller.ko_createKonto(help, a.getText(), b.getText());
                //TODO:
            }

        });

        comboBoxPane.add("Center", tabbedPane);
        comboBoxPane.add("South", ok);
        secondaryScreen.add(comboBoxPane);
        secondaryScreen.setResizable(true);
        secondaryScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        secondaryScreen.setBounds(mainFrame.getX(), mainFrame.getY(), 330, 200);
        secondaryScreen.setVisible(true);
    }

    /**
     * Konto auswählen - Bildschirmmaske
     */
    public void auswahlScreen() {
        if (controller.k_anzahlKonten() == 0) {
            mtextarea.append("\n" + language.trans("nokonto"));
            return;
        }
        secondaryScreen = new JDialog(mainFrame, true);
        secondaryScreen.setTitle("Konto auswählen");
        secondaryScreen.setBackground(Color.WHITE);
        secondaryScreen.setLayout(new BorderLayout());
        JLabel dopp = new JLabel("Bitte Auswahl mit Doppelklick oder ENTER bestätigen", SwingConstants.CENTER);
        dopp.setFont(new Font("", Font.BOLD, 14));
        dopp.setBackground(Color.WHITE);
        secondaryScreen.add(BorderLayout.PAGE_START, dopp);

        //Doppelschritt: 1.Nur die Kontonummern im Array
        final long[] nums = controller.k_getNummern();

        //2. Schritt der komplette Datensatz
        final String[] kontos = controller.k_listeKontos();

        DefaultTableModel dtm = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        dtm.addColumn("Konto");
        dtm.addColumn("Kontentyp");
        dtm.addColumn("Erstellt");
        dtm.addColumn("Saldo");

        for (String str : kontos) {
            String[] values = str.split("°°");
            //Alternative
            //dtm.addRow(new String[] { values[0],values[1],values[2],values[3]});
            dtm.addRow(values);
        }

        final JTable table = new JTable(dtm);
        //					row, col
        table.changeSelection(0, 0, false, false);

        table.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            controller.ko_choose(nums[table.getSelectedRow()]);
                            kontoChosen = true;
                            mtextarea.append("\n" + language.trans("chokonto") + ": " + controller.ko_getNr());
                            statusBarText();
                            closeSecondScreen();
                        }
                    }
                }
        );

        table.addKeyListener(new java.awt.event.KeyAdapter() {
                                 public void keyPressed(KeyEvent ke) {
                                     if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                                         try {
                                             controller.ko_choose(nums[table.getSelectedRow()]);
                                         } catch (Exception e) {
                                         }

                                         kontoChosen = true;
                                         mtextarea.append("\n" + language.trans("chokonto") + ": " + controller.ko_getNr());
                                         statusBarText();
                                         closeSecondScreen();
                                     }
                                     if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                                         closeSecondScreen();
                                     }
                                 }
                             }
        );

        table.setShowVerticalLines(false);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);

        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumn("Saldo").setCellRenderer(dtcr);

        JScrollPane scr = new JScrollPane(table);

        secondaryScreen.add("Center", scr);

        DecimalFormat df = new DecimalFormat("##,##0.00 \u00A4\u00A4");
        String saldo = String.format("%16s\n", df.format(controller.k_gesamtSaldo()));

        JLabel ssaldo = new JLabel("Liquide Mittel: " + saldo, SwingConstants.RIGHT);
        ssaldo.setFont(new Font("Lucida Console", Font.PLAIN, 16));
        ssaldo.setBackground(Color.WHITE);

        secondaryScreen.setBackground(Color.white);
        secondaryScreen.add("South", ssaldo);
        secondaryScreen.setResizable(false);
        secondaryScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        secondaryScreen.setBounds(mainFrame.getX(), mainFrame.getY(), 600, 80 + kontos.length * 20);
        secondaryScreen.setVisible(true);
    }

    /**
     * Aktualisiert den Statustext
     */
    public void statusBarText() {
        String formArbeit = controller.k_getArbeit();
        if (kontoChosen) formArbeit += " | " + language.trans("knummer") + " " + controller.ko_getNr();
        status.setText(" " + language.trans("connuser") + " " + controller.k_getFullname() + " | " + language.trans("tb_workday") + ": " + formArbeit);
    }

    /**
     * @param languageStr Neue Sprache festlegen
     */
    public void changeLanguage(String languageStr) {
        // Check if new and old are different
        if (!language.getLanguageStr().equals(languageStr)) {
            language = new Language(languageStr);
            BankSettingsDTO bankSettingsDTO = saveAsDto();
            controller.getSettingsController().saveSettings(bankSettingsDTO);
            controller.setBankSettingsDTO(bankSettingsDTO);
            mainFrame.dispose();
            mainFrame.removeAll();
            System.gc();
            run();
        }
    }

    public void logon() {
        // Pruefen, ob etwas eingegeben wurde
        if (felduser.getText().isEmpty() || String.valueOf(feldpass.getPassword()).isEmpty()) return;

        //Variablen zwischenspeichern, die originalen werden geloescht
        String user = felduser.getText();
        String pass = String.valueOf(feldpass.getPassword());

        // Erst abmelden, dann kommt die Anmeldung
        logoff();

        int lev = controller.dbLogin(user, pass);

        // Verbindungsprobleme
        if (lev == 1) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, language.trans("noConn"),
                    "Connection Error", JOptionPane.WARNING_MESSAGE);
        }

        // Login falsch
        if (lev == 2) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ef) {
                return;
            }
            JOptionPane.showMessageDialog(null, language.trans("noaccess"),
                    "Error 404", JOptionPane.ERROR_MESSAGE);
        }

        // User ist drin
        if (lev == 0) {
            mtextarea.setText(language.trans("user") + " " + controller.k_getFullname() + " " + language.trans("userconn"));
            if (secondaryScreen != null) {
                closeSecondScreen();
            }
            statusBarText();
            loadBankToolbar();
        }
    }


    // TODO: Logik einbauen
    public String barZahlung(int richtung, String sbetrag) {
        double dbetrag;
        if (sbetrag.contains(","))
            sbetrag = sbetrag.replace(',', '.');
        try {
            dbetrag = Double.parseDouble(sbetrag);
        } catch (Exception e) {
            return language.trans("wrongtrans");
        }

        StringBuilder result = new StringBuilder();
        double max = 19000; //@TODO: Double.parseDouble(bankLogic.get("zahlung"));
        if (dbetrag >= max) {
            result.append("\n").append(language.trans("toohigh")).append(" ").append(dbetrag);
            return result.toString();
        }
        if (dbetrag >= 30000 && dbetrag < max)
            result.append("\n").append(language.trans("tax30"));

        if (richtung == 1) {
            if (controller.einzahlen(dbetrag)) {
                result.append("\n").append(language.trans("income"));
            }
        }

        if (richtung == 0) {
            if (controller.auszahlen(dbetrag)) {
                result.append("\n").append(language.trans("outcome"));
            }
        }

        /*
         * if(rich==1) { if(konto.konto_typ().equals("FEST")) {
         * mtextarea.append(
         * "\nKeine Einzahlung auf laufende Festgeldkonten m�glich"); }
         * if(konto.einzahlen(betrag))mtextarea.append(
         * "\nEinzahlvorgang abgeschlossen!"); }
         *
         * if(richtung==0&&help!=9999) {
         * if(konto.auszahlen(betrag))mtextarea.append(
         * "\nAuszahlvorgang abgeschlossen!"); }
         */
        result.append("\n").append(language.trans("transfinish"));
        return result.toString();
    }

    /**
     * Abmelden des Users
     */
    public void logoff() {
        if (controller.isLoggedIn()) {
            toolbar.setVisible(false);
            felduser = null;
            feldpass = null;
            mtextarea.setText(language.trans("user") + " " + controller.k_getFullname() + " " + language.trans("logofft") + "\n" + language.trans("welcomever"));
            status.setText(language.trans("logoffs"));
            console.setText("");
            controller.dbLogoff();
            kontoChosen = false;
        }
    }

    /**
     * About-screen
     */
    public void aboutScreen() {
        new AboutDialog(mainFrame);
    }

    /**
     * Konsolenbefehle
     *
     * @param comm String von der Kommandozeile
     */
    private void execute(String comm) {
        console.setEnabled(false);
        if (comm.equals("conn")) {
            mtextarea.append("\n" + language.trans("connmess") + "\n" + language.trans("plwait") + "\n");
            mtextarea.append(controller.connTest());
        }
        if (comm.equals("on")) {
            logoff();
            logonScreen();
        }
        if (comm.equals("about")) {
            aboutScreen();
        }
        if (comm.equals("set")) settingsScreen(true);
        if (comm.equals("size")) mainFrame.setBounds(70, 70, 600, 380);
        if (comm.equals("save")) controller.getSettingsController().saveSettings(saveAsDto());
//        if(comm.equals("getconn")) my.getConn();

        if (controller.connected()) {
            if (comm.equals("off")) logoff();
            if (comm.equals("saldo")) mtextarea.append("\n" + controller.k_gesamtSaldo());
            if (comm.equals("sel")) auswahlScreen();
            if (comm.equals("ein")) paymentScreen("ein");
            if (comm.equals("aus")) paymentScreen("aus");
            if (comm.equals("kon")) accountView();
            if (comm.equals("ums")) salesView();
            if (comm.equals("acc")) mtextarea.append("\nKonten: " + controller.k_anzahlKonten());
//            if (comm.equals("work")) workingDayScreen();

        }

        if (comm.equals("cls")) mtextarea.setText(language.trans("welcomever") + "\n");
        console.setEnabled(true);
        console.requestFocus();
        if (comm.equals("exit") || comm.equals("x")) if (controller.connected()) {
            controller.quit();
        }
    }

    public void registerActionListener(BankController controller) {
        this.controller = controller;
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
                language.getLanguageStr(),
                mainFrame.getBounds().x,
                mainFrame.getBounds().y
        );
    }

    public void connectionTestView() {
        mtextarea.append("\n" + language.trans("connmess") + "\n" + language.trans("plwait") + "\n");
        mtextarea.append(controller.connTest());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println(cmd);

        if (cmd.equals("beenden")) {
            logoff();
            controller.quit();
        }
        // @TODO: load all languages
        if (cmd.equals("lang")) {
            if (e.getSource().toString().contains("Deutsch")) changeLanguage("de");
            if (e.getSource().toString().contains("English")) changeLanguage("en");
            if (e.getSource().toString().contains("Türkiye")) changeLanguage("tr");
        }

        if (cmd.equals("F.A.Q.")) {
            System.out.println("faq wurde angeklickt");
        }
    }

    private void selectDriver() {
        switch (dropDown.getSelectedIndex()) {
            case 0:
                dbdriver.setText("org.gjt.mm.mysql.Driver");
                dbport.setText("3306");
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
}
