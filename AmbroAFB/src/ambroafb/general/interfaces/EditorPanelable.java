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
    
    public abstract EditorPanelable cloneWithoutID();
    public abstract EditorPanelable cloneWithID();
    public abstract void copyFrom(EditorPanelable other);
    public abstract boolean compares(EditorPanelable backup);
    
    /** The method uses to search EditorPanelable elements in list (ex. tableView list)
     * @return String to convenient searching.
     */
    public abstract String toStringForSearch();
    
    public int getRecId(){
        return recId;
    };
    
    public void setRecId(int recId){
        this.recId = recId;
    };
    
    
}
