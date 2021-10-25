package view;

import model.AppConstants;
import model.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MyBankToolBar extends JToolBar {

    private final Language language;
    private JCheckBox consoleCheckbox;

    public MyBankToolBar(Language language) {
        super(SwingConstants.VERTICAL);
        this.language = language;
    }

    /**
     * If logged in, toolbar will be displayed
     */
    public void loadBankToolbar() {
        removeAll();
        JButton tbChoose = new JButton(language.trans("tb_choose"));
        tbChoose.setFont(new Font("", Font.BOLD, 12));
        tbChoose.setToolTipText(language.trans("tb_first"));
        tbChoose.setActionCommand(AppConstants.TB_SELECT_ACCOUNT);
//        tbChoose.addActionListener(e -> auswahlScreen());
        tbChoose.setMnemonic(KeyEvent.VK_1);

        JButton ktnew = new JButton(language.trans("tb_create"));
//        ktnew.addActionListener(e -> createAccountScreen());
        ktnew.setMnemonic(KeyEvent.VK_2);

        JButton ktdel = new JButton(language.trans("tb_close"));
        ktdel.setMnemonic(KeyEvent.VK_3);

        JButton einzahlen = new JButton(language.trans("tb_in"));
//        einzahlen.addActionListener(e -> paymentScreen("ein"));
        einzahlen.setMnemonic(KeyEvent.VK_4);

        JButton abheben = new JButton(language.trans("tb_out"));
//        abheben.addActionListener(e -> paymentScreen("aus"));
        abheben.setMnemonic(KeyEvent.VK_5);

        JButton uberweisung = new JButton(language.trans("tb_transfer"));

        JButton umsatz = new JButton(language.trans("tb_sales"));
//        umsatz.addActionListener(e -> salesView());
        umsatz.setMnemonic(KeyEvent.VK_7);

        JButton kinfo = new JButton(language.trans("tb_info"));
//        kinfo.addActionListener(e -> accountView());
        kinfo.setMnemonic(KeyEvent.VK_8);

        JButton festgeld = new JButton(language.trans("tb_fix"));
        festgeld.setMnemonic(KeyEvent.VK_F);

        JButton sparbuch = new JButton(language.trans("tb_save"));

        JButton workday = new JButton(language.trans("tb_workday"));
        workday.setActionCommand(AppConstants.TB_WORK_DAY);
        workday.setMnemonic(KeyEvent.VK_W);

        consoleCheckbox = new JCheckBox(" " + language.trans("tb_console"), false);
        consoleCheckbox.setMnemonic(KeyEvent.VK_C);

        add(tbChoose);
        add(ktnew);
        add(ktdel);
        addSeparator();
        add(einzahlen);
        add(abheben);
        add(uberweisung);
        add(umsatz);
        add(kinfo);
        addSeparator();
        add(festgeld);
        addSeparator();
        add(sparbuch);
        addSeparator();
        add(workday);
        addSeparator();
        add(consoleCheckbox);
        setRollover(true);
        setToolTipText(language.trans("toolbartip"));
    }

    public ArrayList<JButton> getAllButtons() {
        ArrayList<JButton> listComponents = new ArrayList<>();

        for (Component c : this.getComponents()) {
            if (c instanceof JButton) {
                listComponents.add((JButton) c);
            }
        }

        return listComponents;
    }

    public JCheckBox getConsoleCheckbox() {
        return consoleCheckbox;
    }
}
