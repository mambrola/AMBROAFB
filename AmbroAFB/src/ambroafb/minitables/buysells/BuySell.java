/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.buysells;

import ambro.AView;
import ambroafb.general.DBUtils;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class BuySell extends EditorPanelable {

    @AView.Column(title = "rec_id", width = "50", styleClass = "textRight")
    private final StringProperty rec_id;
    
    @AView.Column(title = "%descrip", width = "100")
    private final StringProperty descrip;
    
    private static final String DB_TABLE_NAME = "process_buysells";
    
    public BuySell() {
        rec_id = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
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
    
    
    // Properties:
    public StringProperty recIdProperty(){
        return rec_id;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    
    // Getters:
    @Override
    public int getRecId(){
        return Utils.getIntValueFor(rec_id.get());
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    
    // Setters:
    @Override
    public void setRecId(int recId){
        this.rec_id.set("" + recId);
    }
    
    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
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

    @Override
    public void copyFrom(EditorPanelable other) {
        BuySell otherBuySell = (BuySell) other;
        setDescrip(otherBuySell.getDescrip());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        BuySell backupBuySell = (BuySell) backup;
        return getDescrip().equals(backupBuySell.getDescrip());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
}
