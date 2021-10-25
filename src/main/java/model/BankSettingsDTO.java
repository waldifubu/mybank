package model;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Created by Waldi on 05.02.2016.
 */
public class BankSettingsDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 18765465L;

    private int dbIndex;
    private String dbHost;
    private String dbDatabase;
    private String dbDriver;
    private String dbUser;
    private String dbPort;
    private String dbPassword;
    private String applicationLanguage;

    // Postion of main window
    private Point position;

    public BankSettingsDTO(
            int dbindex,
            String dbHost,
            String dbDatabase,
            String dbDriver,
            String dbUser,
            String dbPort,
            String dbPassword,
            String applicationLanguage,
            int x,
            int y
    ) {
        this.dbIndex = dbindex;
        this.dbHost = dbHost;
        this.dbDatabase = dbDatabase;
        this.dbDriver = dbDriver;
        this.dbUser = dbUser;
        this.dbPort = dbPort;
        this.dbPassword = dbPassword;
        this.applicationLanguage = applicationLanguage;
        this.position = new Point(x, y);
    }

    public BankSettingsDTO() {

    }

    public String getApplicationLanguage() {
        return applicationLanguage;
    }

    public void setApplicationLanguage(String applicationLanguage) {
        this.applicationLanguage = applicationLanguage;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getDbDatabase() {
        return dbDatabase;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPort() {
        return dbPort;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
