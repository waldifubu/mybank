package au_opencsv;

import model.Language;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * @deprecated
 */
public class Einlesen {

    private Language la;

    public Einlesen() {
        la = new Language("de");
        put("de");
//        la.save("de");
        System.out.println(la.size());

        la = new Language("en");
        put("en");
//        la.save("en");
        System.out.println(la.size());
    }

    public static void main(String[] args) {
        new Einlesen();
    }

    private void put(String lang) {
        CSVReader reader = null;
        try {

            reader = new CSVReader(new FileReader("liste_" + lang + ".csv"), ';');
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String[] nextLine;
        try {
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                //System.out.println(nextLine[0] +" "+ nextLine[1]);
                la.add(nextLine[0], nextLine[1]);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
