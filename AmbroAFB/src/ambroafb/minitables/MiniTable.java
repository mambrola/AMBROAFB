/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables;

import ambro.AView;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public abstract class MiniTable extends EditorPanelable {
    
//    @AView.Column(title = "rec_id", width = "50", styleClass = "textRight")
    protected StringProperty rec_id = new SimpleStringProperty("");
    
    @AView.Column(title = "%descrip", width = "100")
    protected StringProperty descrip = new SimpleStringProperty("");
    

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
    public void copyFrom(EditorPanelable other) {
        MiniTable otherMiniTable = (MiniTable) other;
        setDescrip(otherMiniTable.getDescrip());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        MiniTable backupotherMiniTable = (MiniTable) backup;
        return getDescrip().equals(backupotherMiniTable.getDescrip());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
}
