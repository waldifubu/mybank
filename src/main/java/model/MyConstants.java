package model;

public class MyConstants {
    private final static String settingsFile = "settings.cfg";
    private final static String imagePath = "/images/";
    private final static String langPath = "/lang/";
    private final static String rulesFile = "rules.xml";
    private final static String secretKey = "Waldi";

    public static String getSettingsFile() {
        return settingsFile;
    }

    public static String getImagePath() {
        return imagePath;
    }

    public static String getLangPath() {
        return langPath;
    }

    public static String getRulesFile() {
        return rulesFile;
    }

    public static String getSecretkey() {
        return secretKey;
    }
}
