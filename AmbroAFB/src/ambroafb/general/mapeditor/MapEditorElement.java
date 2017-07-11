/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.mapeditor;

/**
 *
 * @author dato
 */
public interface MapEditorElement {
    
    public String getKey();
    public String getValue();
    
    public void setKey(String key);
    public void setValue(String value);
    
    public int compare(MapEditorElement other);
}
