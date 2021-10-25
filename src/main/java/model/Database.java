package model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;

/**
 * @deprecated
 */
public class Database {

    // Connection init
    private Connection connection;

    // DB data
    private String dbhost;
    private String dbuser;
    private String dbpass;
    private String dbname;
    private String dbdriver;

    private BankSettingsDTO bankSettingsDTO;

    // Type of db
    private int dbindex;
    private int dbport;

    // Connection test result
    private boolean online;

    // Connection String
    private String dbUrl;

    // DB Query variables
    private Statement statement;

    public Database(BankSettingsDTO bankSettingsDTO) {
        try {
            dbindex = bankSettingsDTO.getDbIndex();
            dbdriver = bankSettingsDTO.getDbDriver();
            dbhost = bankSettingsDTO.getDbHost();
            dbport = Integer.parseInt(bankSettingsDTO.getDbPort());
            dbuser = bankSettingsDTO.getDbUser();
            dbpass = bankSettingsDTO.getDbPassword();
            dbname = bankSettingsDTO.getDbDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOnline() {
        return online;
    }

    public String connectTest() {
        //S1: Treiber laden
        StringBuilder response = new StringBuilder("Step 1: Loading driver... ");
        try {
            Class.forName(dbdriver).getDeclaredConstructor().newInstance();
            response.append("driver is loaded\n");
        } catch (Exception e) {
            response.append("Unable to load driver\nPlease get the right driver\n");
            return response.toString();
        }

        //S2: Gucken ob Server läuft
        try {
            long tm = System.nanoTime();
            response.append("Step 2: Checking host... ");
            InetAddress host = InetAddress.getByName(dbhost);
            int port = dbport;
            Socket so = new Socket(host, port);
            so.close();
            tm = (System.nanoTime() - tm) / 1000000L;
            response.append("isOnline\n    Host Address = ").append(host.getHostAddress()).append("\n").append("    Host Name    = ")
                    .append(host.getHostName()).append("\n").append("Response Time = ").append(tm).append(" ms\n");
        } catch (IOException es) {
            //2b: Wir testen ob Webserver läuft
            response.append("Connection is dead\n");
            try {
                InetAddress host;
                host = InetAddress.getByName(dbhost);
                Socket so = new Socket(host, 80);
                so.close();
                response.append("Webserver is running, but no DBMS\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        //S3: Verbindung aufbauen, bis hier war alles ok!
        try {
            response.append("Step 3: Trying to connect to DB... ");
            /**
             * selected index:
             * @param 0 = MySQL
             * @param 1 = Oracle
             * @param 2 = MSSQL
             **/
            switch (dbindex) {
                case 0:
                    dbUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;
                    break;
            }
            connection = DriverManager.getConnection(dbUrl, dbuser, dbpass);
            response.append("Connection established\n");

            // Ohne Anmeldung wird die Verbindung geschlossen, da es nur ein Test ist
            if (!online) {
                connection.close();
            }

            //TRUE Verbindung möglich
            online = true;
            return response.toString();
        } catch (com.mysql.cj.jdbc.exceptions.CommunicationsException e) {
            if (e.getErrorCode() == 1049) response.append("Database is not accessible!\n");
            return response.toString();
        } catch (SQLException e) {
            return "Fatal error: Error in settings!";
        }
    }

    public boolean alter(String abfrage) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(abfrage);
            statement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public ResultSet myQuery(String abfrage) {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(abfrage);
            if (rs.wasNull()) return null;
            return rs;
        } //try
        catch (Exception ex) {
            ex.getMessage();
        }
        return null;
    }

    public void close() {
        try {
            online = false;
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userConn(String user, String pass) {
        // Credentials will be checked
        try {
            Class.forName(dbdriver).getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(dbUrl, dbuser, dbpass);
        } catch (Exception e) {
            return false;
        }

        try {
            ResultSet rs = myQuery("select u_pass from users where u_nick='" + user + "'");
            String found;
            while (rs.next()) {
                found = rs.getString("u_pass");
                if (rs.wasNull()) return false;
                else {
                    if (pass.equals(found)) {
                        rs.close();
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQL-Error: Error not found");
        }
        return false;
    }

}
