/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.permanences;

import ambroafb.general.DBUtils;
import ambroafb.minitables.MiniTable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class Permanence extends MiniTable {

    
    private static final String DB_TABLE_NAME = "process_permanences";
    
    public Permanence() {
    }
    
    // DB methods:
    public static ArrayList<Permanence> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(Permanence.class, DB_TABLE_NAME, params);
    }
    
    public static Permanence getOneFromDB(int id){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(Permanence.class, DB_TABLE_NAME, params);
    }
    
    public static Permanence saveOneToDB(Permanence permanence){
        if (permanence == null) return null;
        return DBUtils.saveObjectToDBSimple(permanence, DB_TABLE_NAME);
    }
    
    public static boolean deleteOneFromDB(int id){
        System.out.println("delete from DB... ??");
        return false;
    }
    
}
