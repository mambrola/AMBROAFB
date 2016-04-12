/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

//import ambro.AConnectionToDB;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert.AlertType;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * პასუხისმგებელია აპლიკაციაში არსებული კონფიგურაციების მენეჯმენტზე
 *
 * @author tabramishvili
 */
public class GeneralConfig {

    private static GeneralConfig config;

    /**
     *
     * @return
     */
    public static GeneralConfig getInstance() {
        if (config == null) {
            SavedConfig conf = readConfig();
            if (conf == null) {
                config = new GeneralConfig();
            } else {
                config = new GeneralConfig(conf);
            }
        }
        return config;
    }

    private static SavedConfig readConfig() {
        SavedConfig conf = null;
        try (FileInputStream fileIn = new FileInputStream(Names.GENERAL_CONFIGURATION_FILE_NAME);
                ObjectInputStream streamIn = new ObjectInputStream(fileIn)) {
            conf = (SavedConfig) streamIn.readObject();
        } catch (FileNotFoundException | ClassNotFoundException ex) {
//            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.CONFIGURATION_FILE_OR_CLASS_NOT_FOUND);
//            alert.showAlert();
        } catch (IOException ex) {
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_CONFIGURATION);
            alert.showAlert();
        }
        return conf;
    }

    public ResourceBundle bundle;
    public Locale locale;
    private KFZClient client;

    private ConnectionPool pool;
    private HashMap<String, Object> attributes;
    private SavedConfig savedConf;
    private static final HashMap<String, String> languageIdToName;

    static {
        languageIdToName = new HashMap<>();
        languageIdToName.put("en", "English");
        languageIdToName.put("ka", "ქართული");
    }

    private GeneralConfig() {
        this(new SavedConfig(Locale.getDefault().getLanguage()));
    }

    private GeneralConfig(SavedConfig conf) {
        this(new Locale(conf.language));
        this.savedConf = conf;
    }

    private GeneralConfig(Locale locale) {
        this.locale = locale;
        bundle = loadBundle(locale);
        attributes = new HashMap<>();
    }

    private ResourceBundle loadBundle(Locale locale) {
        return ResourceBundle.getBundle(Names.BUNDLE_TITLES_NAME, locale);
    }

    public KFZClient getServerClient() {
        if (client == null) {
            try {
                client = new KFZClient("sad", "fgh");
            } catch (IOException | KFZClient.KFZServerException ex) {
                Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return client;
    }

    public void logoutServerClient() {
        if (client != null) {
            client.logout();
        }
    }

    /**
     * აბრუნებს აპლიკაციის მიმდინარე Locale ობიექტს
     *
     * @return
     */
    public Locale getCurrentLocal() {
        return locale;
    }

    /**
     * აბრუნებს მიმდინარე ResourceBundle ობიექტს
     *
     * @return
     */
    public ResourceBundle getBundle() {
        return bundle;
    }

    /**
     * აბრუნებს გადმოცემული სტრინგის შესაბამის სტრინგს მიმდინარე ენის მიხედვით
     *
     * @param key
     * @return
     */
    public String getTitleFor(String key) {
        if (bundle.containsKey(key)) {
            return StringEscapeUtils.unescapeJava(bundle.getString(key));
        }
        return key;
    }

    /**
     * აბრუნებს გადმოცემული სტრინგის შესაბამის სტრინგს გადმოცემული ენის მიხედვით
     *
     * @param key - სტრინგი, რომლის შესაბამისი მნიშვნელობაც ბრუნდება
     * @param language - ენა, რომლისთვისაც ბრუნდება გადმოცემული სტრინგის
     * მნიშვნელობა ეს ენა უნდა იყოს ადამიანისთვის წაკითხვადი სახით. მაგ:
     * English, ქართული ...
     * @return
     */
    public String getTitleForLanguage(String key, String language) {
        String id = mapLanguageToId(language);
        ResourceBundle b = loadBundle(new Locale(id));
        return StringEscapeUtils.unescapeJava(b.getString(key));
    }

    /**
     * მიმდინარე კონფიგურაციის მონაცემებს ინახავს მყარ დისკზე
     */
    public void dump() {
        try (FileOutputStream fileOut = new FileOutputStream(Names.GENERAL_CONFIGURATION_FILE_NAME);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(savedConf);
        } catch (IOException ex) {
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_DUMP);
            alert.showAlert();
        }
    }

    /**
     * ანახლებს კონფიგურაციის მონაცემებს
     *
     * @param language
     * @param database
     * @param username
     * @param password
     * @throws java.sql.SQLException
     */
    public void change(String language, String database, String username, String password) throws SQLException {
        if (pool != null) {
            PoolProperties p = new PoolProperties();
            p.setUrl(database);
            p.setDriverClassName(savedConf.classForName);
            p.setUsername(username);
            p.setPassword(password);

            pool = new ConnectionPool(p);
        }
        String lang = mapLanguageToId(language);
        savedConf.language = lang;
        savedConf.database = database;
        savedConf.username = username;
        savedConf.password = password;

    }

    /**
     * აბრუნებს მიმდინარე ენას ადამიანისთვის წაკითხვადი სახით. მაგ: English,
     * ქართული ...
     *
     * @return
     */
    public String getLanguage() {
        return mapIdToLanguage(savedConf.language);
    }

    /**
     * ქმნის და აბრუნებს ახალ კავშირს ბაზასთან მიმდინარე პარამეტრების მიხედვით
     *
     * @return
     * @throws java.sql.SQLException
     */
    public Connection getConnectionToDB() throws SQLException {
        if (pool == null) {
            PoolProperties p = new PoolProperties();
            p.setUrl(savedConf.database);
            p.setDriverClassName(savedConf.classForName);
            p.setUsername(savedConf.username);
            p.setPassword(savedConf.password);
            pool = new ConnectionPool(p);
        }
        return pool.getConnection();
    }

    public String getDatabase() {
        return savedConf.database;
    }

    public String getUsername() {
        return savedConf.username;
    }

    public String getPassword() {
        return savedConf.password;
    }

    public Sizes getSizeFor(String stageName) {
        return savedConf.sizes.get(stageName);
    }

    public void setSizeFor(String stageName, double width, double height) {
        setSizeFor(stageName, new Sizes(width, height, false));
    }

    public void setSizeFor(String stageName, boolean maximized) {
        Sizes size = getSizeFor(stageName);
        if (size == null) {
            size = new Sizes(-1, -1, true);
        }
        size.maximized = maximized;
        setSizeFor(stageName, size);
    }

    public void setSizeFor(String stageName, Sizes size) {
        //savedConf.sizes.put(stageName, size); //Disable by Murman
    }

    /**
     * გადმოცემული key-ს მიხედვით ინახავს გადმოცემულ მნიშვნელობას, რომლის გაგება
     * შემდგომში მთელი აპლიკაციიდან იქნება შესაძლებელი
     *
     * @param key
     * @param value
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * აბრუნებს გადმოცემული key-ს შესაბამის მნიშვნელობას
     *
     * @param key
     * @return
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * შლის გადმოცემული key-ს შესაბამის მნიშვნელობას
     *
     * @param key
     * @return
     */
    public Object removeAttribute(String key) {
        return attributes.remove(key);
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    private static String mapLanguageToId(String language) {
        for (String key : languageIdToName.keySet()) {
            if (languageIdToName.get(key).equals(language)) {
                return key;
            }
        }
        return null;
    }

    private static String mapIdToLanguage(String id) {
        return languageIdToName.get(id);
    }

    /**
     * მინიმალური პარამეტრები, რომლებიც საჭიროა აპლიკაციის მთლიანი პარამეტრების
     * აღსადგენად
     */
    private static class SavedConfig implements Serializable {

        public String classForName = "com.mysql.jdbc.Driver";
        public String language;
        public String database;
        public String username;
        public String password;

        public HashMap<String, Sizes> sizes;

        public SavedConfig(String lan) {
            language = lan;
            database = "jdbc:mysql://localhost:3306/ambro_soft_afb";
            username = "dtm";
            password = "Dat0Tok@Murman1";
            sizes = new HashMap<>();
        }
    }

    /**
     * stage-ის ზომების შემნახველი კლასი
     */
    public static class Sizes implements Serializable {

        public double width;
        public double height;
        public boolean maximized;

        public Sizes(double width, double height, boolean maximized) {
            this.width = width;
            this.height = height;
            this.maximized = maximized;
        }
    }
}
