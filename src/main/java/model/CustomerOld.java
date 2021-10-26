package model;

import controller.BankController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomerOld {
    private long id;
    private String nick;
    private String fullname;
    private String arbeit;

    private double gesamtsaldo;

    private final Database database;
    private BankController bankController;

    public CustomerOld(String username, Database database, BankController m) {
        this.database = database;
        try {
            ResultSet rs = this.database.myQuery("select * from users where u_nick='" + username + "'");

            while (rs.next()) {
                id = Long.parseLong(rs.getString("u_id"));
                nick = rs.getString("u_nick");
                fullname = rs.getString("u_fullname");
                arbeit = rs.getDate("u_arbeit").toString();
                //TODO: IM Release einbauen!
                //dBase.alter("update users set u_last=now() where u_nick='"+username+"'");
            }
            bankController = m;
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public double getGesamtsaldo() {
        ResultSet rs = database.myQuery("select sum(k_saldo) gesamt from konten where k_kunde ='" + getID() + "'");
        try {
            if (rs.next()) gesamtsaldo = rs.getDouble("gesamt");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gesamtsaldo;
    }

    public long getID() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getNick() {
        return nick;
    }

    public String getArbeit() {
        ResultSet rs = database.myQuery("select u_arbeit from users where u_nick='" + getNick() + "'");
        try {
            rs.next();
            arbeit = rs.getDate("u_arbeit").toString();
            rs.close();
        } catch (SQLException e) {
            return null;
        }
        return arbeit;
    }

    public boolean setArbeit(String datum) {
        return database.alter("update users set u_arbeit='" + datum + "' where u_nick='" + getNick() + "'");
    }

    public int countKonto(String typ) {
        String sql = "";
        //if(art.equals("GIRO"))typ=1;
        //if(art.equals("SPAR"))typ=2;
        //if(art.equals("FEST"))typ=3;
        try {
            if (!typ.equals("ALL")) sql = "and k_typ='" + typ + "'";
            ResultSet rs = database.myQuery("select count(*) from konten where k_kunde ='" + getID() + "' " + sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            return 0;
        }
    }

    public int anzahlKonten() {
        return countKonto("ALL");
    }

    public long[] getNummern() {
        int i = 0;
        long[] nummern = new long[5];
        try {
            ResultSet rs = database.myQuery("select k_id from konten where k_kunde ='" + getID() + "' order by k_id desc");
            while (rs.next()) {
                nummern[i++] = rs.getLong("k_id");
            }
        } catch (Exception e) {
            return null;
        }
        return nummern;
    }

    public String[] listeKontos() {
        int i = 0;
        String a, b, c, saldo;
        String[] kontos = new String[5];

        try {
            ResultSet rs = database.myQuery("select * from konten where k_kunde ='" + getID() + "' order by k_id desc");
            while (rs.next()) {
                a = rs.getString("k_id");

                String type = rs.getString("k_typ");
                String sDate = rs.getString("k_made");

                java.util.Date dDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
                SimpleDateFormat sdfTranslated = new SimpleDateFormat(bankController.getModel().getLanguage().trans("dateformat"), Locale.getDefault());
                c = sdfTranslated.format(dDate);

                double zahl = rs.getDouble("k_saldo");
                gesamtsaldo += zahl;
                DecimalFormat df = new DecimalFormat("##,##0.00 \u00A4\u00A4");
                saldo = String.format("%16s\n", df.format(zahl));

                ResultSet res = database.myQuery("select t_name from trans_typ where t_code ='" + type + "'");
                res.next();
                b = res.getString("t_name");
                res.close();

                kontos[i++] = a + "°°" + b + "°°" + c + "°°" + saldo;
            }
            return kontos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    } // liste ende
}
