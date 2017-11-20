/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class SeparateSaving {
    
    public static JSONObject get(String key, Object source){
        return authclient.Utils.toUnderScore(Utils.getJSONFromClass(source));
    }
}
