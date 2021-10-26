package view;

import entity.User;
import model.Language;
import model.MyConstants;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class View extends JFrame implements Runnable {
    // Main textare to show all details
    public JTextArea mtextarea;
    // Menu
    private MyBankMenuBar myBankMenuBar;
    // Temporary second screen
    private JDialog secondaryScreen;
    // Console for power users
    private JTextField consoleInput;
    // If logged on, it's active
    private MyBankToolBar myBankToolBar;
    private JLabel status;
    // Object to get translations
    private Language language;

    // Is account chosen?
    private boolean kontoChosen;

    public View() {
        super("myBank v1.0");

        // Special border for a nice look
        Border compound = BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createLineBorder(Color.GRAY));

        // For debugging and power users we have a nice input field as a "console"
        consoleInput = new JTextField();
        consoleInput.setBorder(compound);
    }

    /**
     * Thread is called, frame with components will be displayed
     */
    @Override
    public void run() {
        // Load primary frame
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "euro.png")));

        // Toolbar for bank functions, movable
        myBankToolBar = new MyBankToolBar(language);
        myBankToolBar.setVisible(false);
        consoleInput.setVisible(false);

        // Create big textarea to see all information
        mtextarea = new JTextArea();
        mtextarea.setFont(new Font("Calibri", Font.PLAIN, 16));
        mtextarea.setText(language.trans("welcomever"));
        mtextarea.setEditable(false);
        mtextarea.setLineWrap(true);
        mtextarea.setBorder(BorderFactory.createLoweredBevelBorder());
        mtextarea.setAutoscrolls(true);

        // Back to 1st idea?
        //        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JScrollPane(mtextarea),console);
        //        split.setResizeWeight(1);

        JScrollPane jp = new JScrollPane(mtextarea);
        Container container = getContentPane();

        container.add(jp, BorderLayout.CENTER);
        container.add(consoleInput, BorderLayout.SOUTH);
        jp.setWheelScrollingEnabled(true);
        jp.setAutoscrolls(true);

        JPanel panel = new JPanel();
        BorderLayout bord = new BorderLayout();
        panel.setLayout(bord);
        panel.add("North", myBankMenuBar);
        panel.add("West", myBankToolBar);

        // container consists of 2 elements
        panel.add("Center", container);

        // statusbar at the bottom
        status = new JLabel(language.trans("welcome"));
        status.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.add("South", status);

        // Last but not least...
        setContentPane(panel);
        // @TODO: Research: is pack same as setbounds?
        // pack();
        setBounds(getX(), getY(), 700, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        toFront();
        setAutoRequestFocus(true);
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public MyBankMenuBar getMyBankMenuBar() {
        return myBankMenuBar;
    }

    public void setMyBankMenuBar(MyBankMenuBar myBankMenuBar) {
        this.myBankMenuBar = myBankMenuBar;
    }

    public void print(String text) {
        mtextarea.append("\n" + text);
    }

    /**
     * Updates text in statusbar
     */
    public void updateStatusBar(User user) {
        //String formArbeit = controller.k_getArbeit();
        //if (kontoChosen) formArbeit += " | " + language.trans("knummer") + " " + controller.ko_getNr();
        status.setText(" " + language.trans("connuser") + " " + user.getFullname() + " | " + language.trans("tb_workday") + ": " + user.getWorkingday().toString());
    }

    public void closeSecondaryScreen() {
        if (secondaryScreen != null) {
            secondaryScreen.dispose();
        }
    }

    public JDialog getSecondaryScreen() {
        return secondaryScreen;
    }

    public void setSecondaryScreen(JDialog secondaryScreen) {
        this.secondaryScreen = secondaryScreen;
    }

    public void loadToolbar() {
        myBankToolBar.loadBankToolbar();
        myBankToolBar.setVisible(true);
    }

    public MyBankToolBar getMyBankToolBar() {
        return myBankToolBar;
    }

    public JTextField getConsoleInput() {
        return consoleInput;
    }

    public void setConsoleInput(JTextField consoleInput) {
        this.consoleInput = consoleInput;
    }
}
