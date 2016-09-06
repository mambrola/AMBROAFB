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
public class Element {
    
    private String key, value;
    
    public Element(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString(){
        return key + " : " + value;
    }
    
    public boolean equals(Element other){
        return key.equals(other.getKey()) && value.equals(other.getValue());
    }
    
}
