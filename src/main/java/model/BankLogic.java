package model;

import util.XMLparser;

import java.util.Properties;


public class BankLogic {
    private XMLparser xml;

    private static Properties props = new Properties();
    private static int zaehler = 0;
    private static BankLogic bankLogic;

    private BankLogic() {
        //Ein Objekt wird erstellt
        zaehler = 1;
        xml = new XMLparser(MyConstants.getRulesFile());
        xml.lesen();
        props = xml.getall();
    }

    public static BankLogic getInstance() {
        return (zaehler == 0) ? bankLogic = new BankLogic() : bankLogic;
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    /*
     * Konto erstellen
     * Nicht mehr als "alle" Konten
     * 0-5 Giro
     * 0-1 Fest
     * 0-1 Spar
     */
    public String anzahlKonten() {
        return get("alle");
    }

    public String anzahlGiro() {
        return get("giro");
    }

    public String anzahlFest() {
        return get("fest");
    }

    public String anzahlSpar() {
        return get("spar");
    }

    public String getMaxSum() {
        return get("zahlung");
    }

	
  /*
   * Barzahlung
   * Ein- Auszahlen 
   * max. summe festlegen    
   * gebuehr
   */

  /*
   * Datum einstellen
   * sprache anpassen
   */

	
  /*
   * Sparbuch Aufzinsung berechnen
   */

  /*
   * Festgeld Aufzinsung berechnen
   */
	
  /*
   * Ueberweisen von Konto 1 zu Konto 2
   */
}
