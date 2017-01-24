/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.permanences;

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
public class Permanence extends EditorPanelable {

    @AView.Column(title = "rec_id", width = "50", styleClass = "textRight")
    private final StringProperty rec_id;
    
    @AView.Column(title = "%descrip", width = "100")
    private final StringProperty descrip;
    
    
    private static final String DB_TABLE_NAME = "process_permanences";
    
    public Permanence() {
        rec_id = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
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
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    
    @Override
    public Permanence cloneWithoutID() {
        Permanence clone = new Permanence();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Permanence cloneWithID() {
        Permanence clone = cloneWithoutID();
        clone.setRecId(getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Permanence otherPermanence = (Permanence) other;
        setDescrip(otherPermanence.getDescrip());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Permanence backupPermanence = (Permanence) backup;
        return getDescrip().equals(backupPermanence.getDescrip());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
}
