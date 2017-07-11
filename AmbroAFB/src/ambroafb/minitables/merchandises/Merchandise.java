/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.merchandises;

import ambroafb.general.DBUtils;
import ambroafb.minitables.MiniTable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class Merchandise extends MiniTable {
    
    private static final String DB_TABLE_NAME = "process_merchandises";
    
    public Merchandise() {
    }
    
    // DB methods:
    public static ArrayList<Merchandise> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        ArrayList<Merchandise> merchandises = DBUtils.getObjectsListFromDB(Merchandise.class, DB_TABLE_NAME, params);
        merchandises.sort((Merchandise s1, Merchandise s2) -> s1.getDescrip().compareTo(s2.getDescrip()));
        return merchandises;
    }
    
    public static Merchandise getOneFromDB(int id){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(Merchandise.class, DB_TABLE_NAME, params);
    }
    
    public static Merchandise saveOneToDB(Merchandise merchandise){
        if (merchandise == null) return null;
        return DBUtils.saveObjectToDBSimple(merchandise, DB_TABLE_NAME);
    }
    
    public static boolean deleteOneFromDB(int id){
        System.out.println("delete from DB... ??");
        return false;
    }
    
//    public static String getDialogStagePath(){
//        return "ambroafb.minitables.dialog.MiniTableDialog";
//    }
    
    
    @Override
    public Merchandise cloneWithoutID() {
        Merchandise clone = new Merchandise();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Merchandise cloneWithID() {
        Merchandise clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }
    
    @Override
    public String toString(){
        return getDescrip();
    }
}
