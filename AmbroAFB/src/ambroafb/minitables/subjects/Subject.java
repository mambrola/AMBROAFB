/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.subjects;

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
public class Subject extends EditorPanelable {
    
    @AView.Column(title = "rec_id", width = "50", styleClass = "textRight")
    private final StringProperty rec_id;
    
    @AView.Column(title = "%descrip", width = "100")
    private final StringProperty descrip;
    
    private static final String DB_TABLE_NAME = "process_subjects";
    
    public Subject() {
        rec_id = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
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
        rec_id.set("" + recId);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    
    @Override
    public Subject cloneWithoutID() {
        Subject clone = new Subject();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Subject cloneWithID() {
        Subject clone = cloneWithoutID();
        clone.setRecId(getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Subject otherSubject = (Subject) other;
        setDescrip(otherSubject.getDescrip());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Subject backupSubject = (Subject) backup;
        return getDescrip().equals(backupSubject.getDescrip());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
}
