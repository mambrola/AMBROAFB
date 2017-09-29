/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import authclient.AuthServerException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class DBExceptionManager {
    
    private int code;
    private String message;
    
    public DBExceptionManager(Exception e){
        if (e instanceof AuthServerException){
            try {
                JSONObject jsObj = new JSONObject(((AuthServerException) e).getMessage());
                code = jsObj.optInt("code");
                message = jsObj.optString("message");
            } catch (JSONException ex) {
                Logger.getLogger(DBExceptionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (e instanceof IOException) {
            System.out.println("DBExceptionManager class! << IOException >>");
                    
        }
    }
    
    public int getExceptionCode(){
        return code;
    }
    
    public String getExceptionMessage(){
        return message;
    }
    
    
}
