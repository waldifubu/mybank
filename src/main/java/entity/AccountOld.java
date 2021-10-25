package entity;

import model.Database;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @deprecated
 */
public class AccountOld {
    private long konto_nr;
    //Kontotyp GIRO SPAR FEST
    private String typ;

    //Erstelldatum
    private String erstellt;

    private BigDecimal kontostand;

    //Eigentümer ID
    @SuppressWarnings("unused")
    private String kunden_id;

    private ResultSet rs;
    private Database link;

    public AccountOld(Database linki) {
        link = linki;
    }

    public boolean kontoCreate(String typ, double a, double b, long kunden_nr) {
        try {
            // Finde freie Konto nr.
            ResultSet rs = link.myQuery("select max(k_id) from konten");
            long fund = 100000;
            if (rs.next() && rs.getBoolean(1)) fund = rs.getLong(1);
            rs.close();
            ++fund;

            String sql = "INSERT INTO konten VALUES (" + fund + ", '" + typ + "', NOW(), 0.0, " + kunden_nr + ")";

            System.out.println(sql);
            if (link.alter(sql)) {
                // 2. Schritt: Beziehungstabelle erstellen
                if (typ.equals("GIRO")) sql = "INSERT INTO giro VALUES (" + fund + "," + a + "," + b + ")";
                if (typ.equals("SPAR")) sql = "INSERT INTO spar VALUES (" + fund + "," + a + ")";
                if (typ.equals("FEST")) sql = "INSERT INTO fest VALUES (" + fund + "," + a + "," + b + ")";
                konto_nr = fund;
                if (link.alter(sql)) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public String kontoTyp() {
        return typ;
    }

    public void getData(long nr) {
        try {
            ResultSet rs = link.myQuery("select * from konten where k_id=" + nr);

            rs.next();
            konto_nr = Long.parseLong(rs.getString("k_id"));
            typ = rs.getString("k_typ");
            erstellt = rs.getDate("k_made").toString();
            kontostand = rs.getBigDecimal("k_saldo");
            kunden_id = rs.getString("k_kunde");
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public long getNr() {
        return konto_nr;
    }

    public String kondition(long nr, String typ) {

        if (typ.equals("GIRO")) {
            try {
                rs = link.myQuery("select * from giro where k_id =" + nr);
                rs.next();
                double a = rs.getDouble("g_uberzug");
                double b = rs.getDouble("g_gebuehr");
                String geb = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(a);
                rs.close();
                return "Gebühren je Quartal: " + geb +
                        "\nÜberzugszinssatz: " + b + "%";
            } catch (Exception e) {
                e.printStackTrace();
                return "\nProbleme bei Konditionsabruf1\n";
            }

        }

        if (typ.equals("FEST")) {
            try {
                rs = link.myQuery("select * from fest where k_id =" + nr);
                rs.next();
                double a = rs.getDouble("f_dauer");
                double b = rs.getDouble("f_zins");
                rs.close();
                return "Laufzeit: " + a + " Jahr(e)" +
                        "\nZins: " + b + "%";
            } catch (Exception e) {
                e.printStackTrace();
                return "\nProbleme bei Konditionsabruf1\n";
            }
        }

        if (typ.equals("SPAR")) {
            try {
                rs = link.myQuery("select * from spar where k_id =" + nr);
                rs.next();
                double a = rs.getDouble("sp_zins");
                rs.close();
                return "Sparzins p.a.: " + a + "%";
            } catch (Exception e) {
                e.printStackTrace();
                return "\nProbleme bei Konditionsabruf1\n";
            }
        }


        return "\nProbleme bei Konditionsabruf2\n";
    }

    public String transCode(String typ) {
        try {
            ResultSet res = link.myQuery("select t_name from trans_typ where t_code ='" + typ + "'");
            res.next();
            return res.getString("t_name");
        } catch (Exception e) {
            return "Propleme";
        }
    }


    public String kontoinfo() {
        // Aktuelle Daten holen
        getData(getNr());
        String arbeitfor = "";
        String saldo = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(kontostand());

        try {
            Date df = new SimpleDateFormat("yyyy-MM-dd").parse(erstellt());
            arbeitfor = new SimpleDateFormat("dd.MM.yyyy").format(df);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return "\n\nInfo über Konto: " + getNr() +
                "\nAngelegt am: " + arbeitfor +
                "\nKontoart: " + transCode(kontoTyp()) +
                "\nKonditionen:\n" +
                kondition(getNr(), kontoTyp()) +
                "\n====================================" +
                "\nDer momentane Kontostand beträgt: " + saldo;
    }

    public BigDecimal kontostand() {
	 /*
	 numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
     numberFormat.setMinimumFractionDigits(2);
     numberFormat.setMaximumFractionDigits(2);
     */
        //return kontostand;
        return kontostand;
    }

    public String erstellt() {
        return erstellt;
    }

    public boolean einzahlen(double betrag) {
        String trans_typ = "";
        if (kontoTyp().equals("GIRO")) trans_typ = "EIN_GIRO";
        if (kontoTyp().equals("SPAR")) trans_typ = "EIN_SPAR";
        if (link.alter("update konten set k_saldo=k_saldo+" + betrag + " where k_id=" + konto_nr)) {
            if (link.alter("insert transaktionen values(null, " + betrag + " ,'', now(), 0, " + konto_nr + ",'" + trans_typ + "')"))
                return true;
        }
        return false;
    }

    public boolean auszahlen(double betrag) {
        String trans_typ = "";
        if (kontoTyp().equals("GIRO")) trans_typ = "GIRO_AUS";
        if (kontoTyp().equals("SPAR")) trans_typ = "SPAR_AUS";
        if (link.alter("update konten set k_saldo=k_saldo-" + betrag + " where k_id=" + konto_nr)) {
            if (link.alter("insert transaktionen values(null, " + betrag + " ,'', now(), " + konto_nr + ",0,'" + trans_typ + "')"))
                return true;
        }
        return false;
    }

    //TODO
    public String umsatz() {
        StringBuilder sb = new StringBuilder("\nUmsätze für das Konto: " + getNr());
        String datum;
        try {
            ResultSet rs = link.myQuery("select count(*) from transaktionen where t_kontoa=" + getNr() + " or t_kontob=" + getNr());
            rs.next();
            if (rs.getLong(1) == 0) {
                rs.close();
                return "\nBisher keine Umsätze vorhanden";
            }

            String type;
            String hl, verwend = null, partner;
            rs = link.myQuery("select * from transaktionen where t_kontoa=" + getNr() + " or t_kontob=" + getNr());
            while (rs.next()) {
                Date df = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getDate("t_zeit").toString());
                datum = new SimpleDateFormat("dd.MM.yyyy").format(df);

                // Wenn T dann ist DIESES konto der Sender
                // wenn F dann ist DAS konto empfänger
                if (rs.getLong("t_kontoa") == getNr()) {
                    hl = "-";
                    if (rs.getString("trans_code").contains("AUS"))
                        partner = "Persönliche Abhebung";
                    else
                        partner = getOwner(rs.getLong("t_kontob"));
                } else {
                    hl = "+";
                    if (rs.getString("trans_code").contains("EIN"))
                        partner = "Persönliche Einzahlung";
                    else
                        partner = getOwner(rs.getLong("t_kontoa"));
                }

                type = transCode(rs.getString("trans_code"));
                if (rs.getString("t_verwend").isEmpty()) verwend = "beleglos";
                else verwend = rs.getString("t_verwend");

                //Datum		| TYP				Betrag +-
                //          | von wem 		| verwendungs
                DecimalFormat deci = new DecimalFormat(",##0.00");
                String betrag = deci.format(rs.getDouble("t_betrag"));


                sb.append("\n" + datum + " | " + type + " | " + betrag + hl + "\n" +
                        "                   | " + partner + " | " + verwend);
            }
            rs.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getOwner(long nr) {
        try {
            ResultSet rs = link.myQuery("select u.u_fullname from users u, konten k where u.id=k.k_kunde and k.k_id=" + nr);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
            return "Probs";
        }
    }

}
