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
    
    public EditorPanelable cloneWithoutID() {return null;}
    public EditorPanelable cloneWithID() {return null;}
    public void copyFrom(EditorPanelable other){};
    public void asignTable(){};
}
