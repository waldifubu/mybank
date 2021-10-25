package au_opencsv;

import model.Language;
import model.MyConstants;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

public class LanguageHelper {

    private Language language;

    public LanguageHelper() {

        language = new Language("de");
        language.setProps(new Properties());
        getFrom("xa", 1);
        save("de");
        System.out.println(language.size() + " de");

        language = new Language("en");
        language.setProps(new Properties());
        getFrom("xa", 2);
        save("en");
        System.out.println(language.size() + " en");
    }

    public static void main(String[] args) {new LanguageHelper();}

    private void getFrom(String languageStr, int spalte) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader("liste_" + languageStr + ".csv"), ';');
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String[] nextLine;

        try {
            while (true) {
                assert reader != null;
                if ((nextLine = reader.readNext()) == null) break;
                // nextLine[] is an array of values from the line
                language.add(nextLine[0], nextLine[spalte]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String lang) {
        /*
         * props.setProperty("welcomever", "Willkommen zu myBank 1.0!");
         */
        String suffix = ".xml";
        String uri = MyConstants.getLangPath() + "lang_" + lang + suffix;
        File f = null;
        URL url = getClass().getResource(uri);

        if (url != null) { //if there is a file in the location
            f = new File(url.getFile());
            if (f.exists()) {
                boolean isDeleted = f.delete();
                if(isDeleted) {
                    System.out.println("File " + f.getAbsolutePath() + " is deleted successfully.");
                } else {
                    System.out.println("File " + f.getAbsolutePath() + " is not deleted.");
                }
            }
        }

        try {
            String comment = "Created: " + Calendar.getInstance().getTime() + "\nLanguage file v1.0.0 for language " + lang;
            Properties props = language.getProps();
            props.storeToXML(new FileOutputStream(f), comment);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
