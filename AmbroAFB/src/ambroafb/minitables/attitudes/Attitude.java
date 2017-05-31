/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes;

import ambroafb.general.DBUtils;
import ambroafb.minitables.MiniTable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class Attitude extends MiniTable {

    private static final String DB_TABLE_NAME = "process_attitudes";
    
    public Attitude() {
    }
    
    //DB methods:
    public static ArrayList<Attitude> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        ArrayList<Attitude> attitudeList = DBUtils.getObjectsListFromDB(Attitude.class, DB_TABLE_NAME, params);
        attitudeList.sort((Attitude b1, Attitude b2) -> b1.getDescrip().compareTo(b2.getDescrip()));
        return attitudeList;
    }
    
    public static Attitude getOneFromDB(int id){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(Attitude.class, DB_TABLE_NAME, params);
    }
    
    public static Attitude saveOneToDB(Attitude attitude){
        if (attitude == null) return null;
        return DBUtils.saveObjectToDBSimple(attitude, DB_TABLE_NAME);
    }
    
    public static boolean deleteOneFromDB(int id){
        System.out.println("delete from DB... ??");
        return false;
    }
    
//    public static String getDialogStagePath(){
//        return "ambroafb.minitables.dialog.MiniTableDialog";
//    }
    
    @Override
    public Attitude cloneWithoutID() {
        Attitude clone = new Attitude();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Attitude cloneWithID() {
        Attitude clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    
    @Override
    public String toString(){
        return getDescrip();
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        Attitude otherAttitude = (Attitude) other;
        return  getRecId() == otherAttitude.getRecId() && compares(otherAttitude);
    }
}
