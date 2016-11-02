/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.helper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public class ClientStatus {
    
    private int recId;
    private final IntegerProperty clientStatusId;
    private String language;
    private final SimpleStringProperty descrip;
    
    public ClientStatus(){
        descrip = new SimpleStringProperty("");
        clientStatusId = new SimpleIntegerProperty(0);
    }
    
    public IntegerProperty clientStatusIdProperty(){
        return clientStatusId;
    }
    
    public StringProperty statusDescripProperty(){
        return descrip;
    }
    
    
    public int getRecId(){
        return recId;
    }
    
    public int getClientStatusId(){
        return clientStatusId.get();
    }
    
    public String getLanguage(){
        return language;
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    
    public void setRecId(int recId){
        this.recId = recId;
    }
    
    public void setLanguage(String language){
        this.language = language;
    }
    
    public void setClientStatusId(int status){
        this.clientStatusId.set(status);
    }
    
    public void setDescrip(String statusDescrip){
        this.descrip.set(statusDescrip);
    }
    
    public boolean equals(ClientStatus other){
        return this.getClientStatusId() == other.getClientStatusId() && this.getDescrip().equals(other.getDescrip());
    }
    
    @Override
    public String toString(){
        return getDescrip();
    }

    public void copyFrom(ClientStatus other) {
        if (other != null){
            setClientStatusId(other.getClientStatusId());
            setDescrip(other.getDescrip());
            
            setRecId(other.getRecId());
            setLanguage(other.getLanguage());
        }
    }
}
