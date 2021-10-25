package model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public class Language {

    private String langStr;
    private Properties props = new Properties();

    public Language(String lang) {
    //        file_check(lang);

        openLanguageFile(lang);
        langStr = lang;
    }

    /**
     * @param lang
     * @deprecated
     */
    private void openLanguageFile(String lang) {
        InputStream u = getClass().getResourceAsStream(MyConstants.getLangPath() + "lang_" + lang + ".xml");
        try {
            props.loadFromXML(u);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean redundancy_check(String key) {
        Enumeration<?> emu = props.propertyNames();
        String altkey = null;
        while (emu.hasMoreElements()) {
            altkey = (String) emu.nextElement();
            if (altkey.equals(key)) {
                System.out.println("Already used key - " + key);
                return true;
            }

        }
        return false;
    }

    public String trans(String key) {
        return props.getProperty(key);
    }

    public int size() {
        return props.size();
    }

    public void add(String a, String b) {
        // if (!redundancy_check(a))
        props.setProperty(a, b);
    }

    public String getLanguageStr() {
        if (langStr == null) {
            langStr = "en";
        }
        return langStr;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}
