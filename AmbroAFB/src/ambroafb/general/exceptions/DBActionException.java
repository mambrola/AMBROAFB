/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class DBActionException extends Exception {
    
    private ResourceBundle bundle;
    private String messageText = "";
    private int code;
    
//    public DBActionException(String message) {
//        this(message, Names.BUNDLE_TITLES_NAME);
//    }
    
    public DBActionException(String message, String bundlePath) {
        this(message, bundlePath, Locale.getDefault());
    }
    
    public DBActionException(String message, String bundleBaseName, Locale locale) {
        super(message);
        try {
            JSONObject json = new JSONObject(message);
            code = json.optInt("code");
            messageText = json.optString("message");
        } catch (JSONException ex) {
            System.err.println(ex.getMessage());
        }
        bundle = ResourceBundle.getBundle(bundleBaseName, locale);
    }
    
    /**
     * The method search  code in bundle and returns appropriate text.
     * @return If there is value for  key (AuthServer  code)  returns value. Otherwise returns original message text from AuthServer message.
     */
    @Override
    public String getLocalizedMessage(){
        return (bundle.containsKey("" + code)) ? bundle.getString("" + code) : messageText;
    }
    
    /**
     * 
     * @return The AuthServer DB exception code.
     */
    public int getCode(){
        return code;
    }
}
