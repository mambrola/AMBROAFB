/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

//import ambro.AConnectionToDB;
import ambroafb.AmbroAFB;
import authclient.AuthClient;
import authclient.db.DBClient;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * პასუხისმგებელია აპლიკაციაში არსებული კონფიგურაციების მენეჯმენტზე
 *
 * @author tabramishvili
 */
public class GeneralConfig {

    private static final String PREFS_LANGUAGE = "saved_language";

    public static final String classForName = "com.mysql.jdbc.Driver";

    private static GeneralConfig config;
    public static final Preferences prefs = Preferences.userNodeForPackage(AmbroAFB.class);

    /**
     *
     * @return ერთადერთ Instance-ს
     */
    public static GeneralConfig getInstance() {
        if (config == null) {
            String lang = prefs.get(PREFS_LANGUAGE, null);
            if (lang == null) {
                config = new GeneralConfig();
            } else {
                config = new GeneralConfig(lang);
            }
        }
        return config;
    }


    /*აქედან დიდი ნაწილი ალბათ არის Utils, თუმცა წვრილ-წვრილი და ბევრი რაღაცაა შეიძლება ცალკე მაგ. UtilsGC(GeneralConfiguration)-ად დარჩეს*/
    public ResourceBundle bundle;
    public Locale locale;
    private KFZClient client;
    private AuthClient authClient;
    private DBClient dbClient;
    private String auth_username, auth_password;
    private String db_username, db_password;

    private HashMap<String, Object> attributes;
    private String language;
    private static final HashMap<String, String> languageIdToName;

    static {
        languageIdToName = new HashMap<>();
        languageIdToName.put("en", "English");
        languageIdToName.put("ka", "ქართული");
    }

    private GeneralConfig() {
        this(Locale.getDefault().getLanguage());
    }

    private GeneralConfig(String lang) {
        this(new Locale(lang));
        this.language = lang;
    }

    private GeneralConfig(Locale locale) {
        this.locale = locale;
        bundle = loadBundle(locale);
        attributes = new HashMap<>();
    }

    private ResourceBundle loadBundle(Locale locale) {
        return ResourceBundle.getBundle(Names.BUNDLE_TITLES_NAME, locale);
    }

    /**
     * პარამეტრებში ალბათ უნდა იყოს არა ბაზის მისამართი, არამედ
     * "KFZ-Server"-ისა, username და password-იც უნდა იყოს უნიკალური თითოეული
     * მომხმარებლისთვის და აქ უნდა გამოიყენებოდეს
     *
     * @return
     */
    public AuthClient getAuthClient() {
        return authClient;
    }

//    public KFZClient getServerClient(String username, String password) throws IOException, KFZClient.KFZServerException {
//        kfz_username = username;
//        kfz_password = password;
//        client = new KFZClient(kfz_username, kfz_password).setClientName("AmbroAFB");
//        return client;
//    }
    
    public AuthClient getAuthClient(String username, String password) {
        auth_username = username;
        auth_password = password;
        authClient = new AuthClient(auth_username, auth_password, authclient.Utils.getDefaultConfigWithClientName("AmbroAFB"));
        dbClient = new DBClient(auth_username, auth_password, authclient.Utils.getDefaultConfig("http://kfz-soft.de/DBService/api", "AmbroAFB"));
        dbClient.withLang(GeneralConfig.getInstance().locale.getLanguage());
        return authClient;
    }
    
    public DBClient getDBClient() {
        return dbClient;
    }
    
//    public DBClient getDBClient(String username, String password) {
//        db_username = username;
//        db_password = password;
//        dbClient = new DBClient(db_username, db_password, authclient.Utils.getDefaultConfigWithClientName("AmbroAFB"));
//        return dbClient;
//    }
    

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
     * ეს უნდა მოხდეს კონფიგურაციის stage-ში არსებული save-restrart ღილაკზე
     * დაჭერისას
     */
    public void dumpIntoPrefs() {
        prefs.put(PREFS_LANGUAGE, language);
        try {
            prefs.sync();
        } catch (BackingStoreException ex) {
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * ანახლებს კონფიგურაციის მონაცემებს განახლება მგონი არ დაგვჭირდება, ეს
     * მეთოდი გაეშვება პროგრამის ჩატვირთვისას
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language = mapLanguageToId(language);
    }

    /**
     * აბრუნებს მიმდინარე ენას ადამიანისთვის წაკითხვადი სახით. მაგ: English,
     * ქართული ... ეს და პირიქით Locale-ს უნდა ქონდეს, არა?
     *
     * @return
     */
    public String getLanguage() {
        return mapIdToLanguage(language);
    }

    /**
     * გადმოცემული key-ს მიხედვით ინახავს გადმოცემულ მნიშვნელობას, რომლის გაგება
     * შემდგომში მთელი აპლიკაციიდან იქნება შესაძლებელი ამ attributes-ს
     * გამოყენების მაგალითი მთელ პროგრამაში არ მოიძებნა !!!!!
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

    /**
     * ისევ ვიტყვი: მგონია Locale-მ უნდა შეძლოს ეს, შეიძლება ვცდები
     *
     * @param language
     * @return
     */
    private static String mapLanguageToId(String language) {
        for (String key : languageIdToName.keySet()) {
            if (languageIdToName.get(key).equals(language)) {
                return key;
            }
        }
        return null;
    }
    
    private static String mapIdToLanguage(String lang){
        return languageIdToName.get(lang);
    }

}
