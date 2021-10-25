package controller;

import model.BankSettingsDTO;
import model.MyConstants;
import org.jasypt.util.text.BasicTextEncryptor;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SettingsController {

    /**
     * @return super secret
     */
    private static String getSecret() {
        String fileName = "secret.txt";
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            assert fileReader != null;
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String secret = bufferedReader.readLine();
                if (secret != null) {
                    return secret;
                } else {
                    throw new IOException();
                }
            }
        } catch (IOException ioe) {
            return MyConstants.getSecretkey();
        }
    }

    /**
     * Load settings from file
     */
    public static BankSettingsDTO loadSettings() {
        Point position = new Point(100, 100);
        BankSettingsDTO bankSettingsDTO = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(MyConstants.getSettingsFile());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            bankSettingsDTO = (BankSettingsDTO) objectInputStream.readObject();
            BasicTextEncryptor bte = new BasicTextEncryptor();
            bte.setPassword(getSecret());
            bankSettingsDTO.setDbPassword(bte.decrypt(bankSettingsDTO.getDbPassword()));
            objectInputStream.close();
        } catch (Exception ignore) {
            File f = new File(MyConstants.getSettingsFile());
            System.gc();

            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Sorry, Connection data are missing!\nCreate new file for settings.",
                    "Ooops: Error 500", JOptionPane.ERROR_MESSAGE);
            if (!f.delete()) {
                System.exit(0);
            }

            // Default values or we are in trouble ;-)
            position.setLocation(100, 100);
        }

        return bankSettingsDTO;
    }

    /**
     * Save settings in file
     *
     * @param bankSettingsDTO {@link BankSettingsDTO}
     */
    public static boolean saveSettings(BankSettingsDTO bankSettingsDTO) {
        BasicTextEncryptor bte = new BasicTextEncryptor();
        bte.setPassword(getSecret());
        bankSettingsDTO.setDbPassword(bte.encrypt(bankSettingsDTO.getDbPassword()));

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(MyConstants.getSettingsFile()));
            objectOutputStream.writeObject(bankSettingsDTO);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException fatal) {
            fatal.printStackTrace();
            return false;
        }

        return true;
    }
}
