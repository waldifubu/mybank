package model;

import util.VersionComparator;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Waldi on 05.02.2016.
 */
public class MyBankEngine {
    private static MyBankEngine ourInstance = new MyBankEngine();

    public static MyBankEngine getInstance() {
        return ourInstance;
    }

    private MyBankEngine() {
    }

    public static void init() {

    }

    // Nur eine Instanz erlaubt
    public static void oneInstance() {
        final File f = new File(System.getProperty("java.io.tmpdir") + "/FileLock.lock");
        if (f.exists() && fileProp()) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Program is already running!", "Program notice",
                    JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // harter Fall: Wenn nichts mehr geht oder VM stirbt
        f.deleteOnExit();
        /**
         * Requests that the file or directory denoted by this abstract pathname
         * be deleted when the virtual machine terminates. Deletion will be
         * attempted only for normal termination of the virtual machine, as
         * defined by the Java Language Specification. Once deletion has been
         * requested, it is not possible to cancel the request. This method
         * should therefore be used with care.
         */

        // softer Fall: Programm normal geschlossen
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                f.delete();
            }
        }));
    }


    public static boolean fileProp() {
        // Bei Windows mehrere Instanzen erlauben
        if (!System.getProperty("os.name").contains("Windows"))
            return true;
        try {
            final File f = new File(System.getProperty("java.io.tmpdir") + "/FileLock.lock");
            // Shell kommando
            Runtime systemShell = Runtime.getRuntime();
            Process output = systemShell.exec("cmd /c dir " + f.getAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(output.getInputStream()));
            String out = "";
            String line;

            int step = 1;
            while ((line = br.readLine()) != null) {
                if (step == 6)
                    out = line;
                step++;
            } // display process output

            try {
                out = out.replaceAll(" ", "");
                // System.out.println("CreationDate: "+out.substring(0,10));
                // System.out.println("CreationTime: "+out.substring(10,15));
                String alt = out.substring(10, 15);

                java.util.Date now = new java.util.Date();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                String jetzt = sdf.format(now);
                if (alt.startsWith(jetzt.substring(0, 2))) {
                    if (alt.endsWith(jetzt.substring(3, 5)))
                        return true;
                }
                return false;

            } catch (StringIndexOutOfBoundsException se) {
                System.out.println("File not found");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    public static boolean checkJavaVersion() {
        // Integer.parseInt(System.getProperty("java.version").substring(2, 3));
        String semVersion = System.getProperty("java.version");
        //int version = Integer.parseInt(System.getProperty("java.specification.version"));

        VersionComparator versionComparator = new VersionComparator();
        if (versionComparator.compare(semVersion, "1.7") > 0)
            return true;
        else {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Java v1.7 or higher is needed!", "Java VM", JOptionPane.ERROR_MESSAGE);
            System.err.println("Wrong Java Version!");
            return false;
        }
    }
}
