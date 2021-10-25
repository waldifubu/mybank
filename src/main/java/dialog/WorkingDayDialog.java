package dialog;

import model.AppConstants;
import model.Language;
import model.MyConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WorkingDayDialog extends ScreenDialog {

    private final JTextField dateTextField;
    private final JButton applyButton;

    public WorkingDayDialog(JFrame mainFrame, Language lang, String workingDay) {
        super(mainFrame, true);
        setTitle(lang.trans("set_work"));
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "euro.png"));
        setIconImage(img);

        dateTextField = new JTextField(workingDay, 10);

        applyButton = new JButton(lang.trans("save"));
        applyButton.setActionCommand(AppConstants.TB_WORK_DAY_SAVE);

        setLayout(new GridLayout(2, 2));
        add(new JLabel("  " + lang.trans("tb_workday") + ": ", SwingConstants.LEFT));
        add(dateTextField);
        add(new JLabel("  Format: " + lang.trans("datestr"), SwingConstants.LEFT));
        add(applyButton);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(mainFrame.getX(), mainFrame.getY(), 260, 80);
    }

    public JTextField getDateTextField() {
        return dateTextField;
    }

    public JButton getApplyButton() {
        return applyButton;
    }
}
