/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.subjects;

import ambroafb.general.DBUtils;
import ambroafb.minitables.MiniTable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class Subject extends MiniTable {
    
    private static final String DB_TABLE_NAME = "process_subjects";
    
    public Subject() {
    }
    
    // DB methods:
    public static ArrayList<Subject> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(Subject.class, DB_TABLE_NAME, params);
    }
    
    public static Subject getOneFromDB(int id){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(Subject.class, DB_TABLE_NAME, params);
    }
    
    public static Subject saveOneToDB(Subject subject){
        if (subject == null) return null;
        return DBUtils.saveObjectToDBSimple(subject, DB_TABLE_NAME);
    }
    
    public static boolean deleteOneFromDB(int id){
        System.out.println("delete from DB... ??");
        return false;
    }
    
}
