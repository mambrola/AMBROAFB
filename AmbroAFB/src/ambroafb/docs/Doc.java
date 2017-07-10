/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.general.DBUtils;
import ambroafb.licenses.License;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Doc {
    
    private static final String DB_TABLE_NAME = "docs";
    
    public Doc(){
    }
    
    public static ArrayList<License> getAllFromDB() {
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(License.class, DB_TABLE_NAME, params);
    }
}
