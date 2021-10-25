package dialog;

import model.Language;
import model.MyConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginDialog extends ScreenDialog {

    private final JButton confirmBtn;

    // Logindata
    private JTextField felduser;
    private JPasswordField feldpass;

    public LoginDialog(JFrame mainWindow, Language my) {
        super(mainWindow, true);
        confirmBtn = new JButton(my.trans("connect"));
        setVisible(false);

        SwingUtilities.invokeLater(() -> {
            try {
                Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(MyConstants.getImagePath() + "euro.png"));
                setIconImage(img);
                setTitle(my.trans("login"));
                setAlwaysOnTop(true);

                getContentPane();
                setLayout(new GridLayout(3, 2));
                add(new JLabel("  " + my.trans("user"), SwingConstants.LEFT));
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
                            dispose();
                        }
                    }
                });

                add(felduser);
                add(new JLabel("  " + my.trans("passwort"), SwingConstants.LEFT));
                feldpass = new JPasswordField(20);

                add(feldpass);
                add(new JLabel(" "));
                add(confirmBtn);

                feldpass.addKeyListener(new KeyListener() {
                    public void keyTyped(KeyEvent e) {
                    }

                    public void keyReleased(KeyEvent e) {
                    }

                    public void keyPressed(KeyEvent e) {
                        int key = e.getKeyCode();
                        if (key == KeyEvent.VK_ESCAPE) {
                            dispose();
                        }
                        if (key == KeyEvent.VK_ENTER) {
                            if (!felduser.getText().isEmpty() && !new String(feldpass.getPassword()).isEmpty()) {
                                confirmBtn.doClick();
                            }
                            // same as actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"logon_connect"));
                        }
                    }
                });
                setResizable(false);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setBounds(mainWindow.getX(), mainWindow.getY(), 220, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public String getUsername() {
        return felduser.getText();
    }

    public String getUserPassword() {
        return new String(feldpass.getPassword()).toString();
    }

    public JButton getConfirmBtn() {
        return confirmBtn;
    }
}