/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.buysells;

import ambroafb.general.DBUtils;
import ambroafb.minitables.MiniTable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class BuySell extends MiniTable {

    StringProperty rec_id = new SimpleStringProperty("");
    
    private static final String DB_TABLE_NAME = "process_buysells";
    
    public BuySell() {
    }
    
    //DB methods:
    public static ArrayList<BuySell> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(BuySell.class, DB_TABLE_NAME, params);
    }
    
    public static BuySell getOneFromDB(int id){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(BuySell.class, DB_TABLE_NAME, params);
    }
    
    public static BuySell saveOneToDB(BuySell buySell){
        if (buySell == null) return null;
        return DBUtils.saveObjectToDBSimple(buySell, DB_TABLE_NAME);
    }
    
    public static boolean deleteOneFromDB(int id){
        System.out.println("delete from DB... ??");
        return false;
    }
    
    @Override
    public BuySell cloneWithoutID() {
        BuySell clone = new BuySell();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public BuySell cloneWithID() {
        BuySell clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

}
