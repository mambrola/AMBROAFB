/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public class LicenseStatus {
    
    private final SimpleStringProperty descrip;
    private int recId;
    private final IntegerProperty licenseStatusId;
    @JsonIgnore
    public String language;
    
    public LicenseStatus(){
        descrip = new SimpleStringProperty("");
        licenseStatusId = new SimpleIntegerProperty(0);
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public IntegerProperty statusIdProperty(){
        return licenseStatusId;
    }
    
    public int getRecId(){
        return recId;
    }
    
    public int getLicenseStatusId(){
        return licenseStatusId.get();
    }
    
//    public String getLanguage(){
//        return language;
//    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    
    public void setRecId(int recId){
        this.recId = recId;
    }
    
    public void setLicenseStatusId(int licenseStatusId){
        this.licenseStatusId.set(licenseStatusId);
    }
    
//    public void setLanguage(String language){
//        this.language = language;
//    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    
    public void copyFrom(LicenseStatus other){
        if (other != null){
            setLicenseStatusId(other.getLicenseStatusId());
            setDescrip(other.getDescrip());
            setRecId(other.getRecId());
        }
    }
    
    
    @Override
    public String toString(){
        return getDescrip();
    }

    public boolean compares(LicenseStatus other) {
        if (other == null) return false;
        return  getLicenseStatusId() == other.getLicenseStatusId() &&
                getDescrip().equals(other.getDescrip());
    }
    
    /**
     * The method compares two LicenseStatus objects.
     * @param other Other object that is not null.
     * @return 
     * @see ambroafb.general.interfaces.EditorPanelable#compareById(ambroafb.general.interfaces.EditorPanelable)  EditorPanelable method "compareById"
     */
    public int compareById(LicenseStatus other){
        return getRecId() - other.getRecId();
    }
}
