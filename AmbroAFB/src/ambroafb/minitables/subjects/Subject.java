/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.subjects;

import ambroafb.general.interfaces.EditorPanelable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public class Subject extends EditorPanelable {
    
    private final StringProperty descrip;
    
    public Subject() {
        descrip = new SimpleStringProperty("");
    }
    
    // Getters:
    public String getDescrip(){
        return descrip.get();
    }
    
    
    // Setters:
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
