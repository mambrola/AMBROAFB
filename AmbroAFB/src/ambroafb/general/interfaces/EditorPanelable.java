/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author mambroladze
 */
public abstract class EditorPanelable {
    @JsonProperty("recId")
    public int recId;
//    private IntegerProperty idProperty = new SimpleIntegerProperty();
    
    public abstract EditorPanelable cloneWithoutID();
    public abstract EditorPanelable cloneWithID();
    public abstract void copyFrom(EditorPanelable other);
    
    // Must return lowercase string.
    public abstract String toStringForSearch();
    
    public int getRecId(){
        return recId;
    };
    
    public void setRecId(int recId){
        this.recId = recId;
//        idProperty.set(recId);
    };
    
    
}
