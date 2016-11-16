/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.helper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
@SuppressWarnings("EqualsAndHashcode")
public class LicenseStatus {
    
    private final SimpleStringProperty descrip;
    private int recId;
    private final IntegerProperty licenseStatusId;
    private String language; // JsonIgnorable ??
    
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
    
    public String getLanguage(){
        return language;
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    
    public void setRecId(int recId){
        this.recId = recId;
    }
    
    public void setLicenseStatusId(int licenseStatusId){
        this.licenseStatusId.set(licenseStatusId);
    }
    
    public void setLanguage(String language){
        this.language = language;
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    
//    @Override
//    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
//    public boolean equals(Object other){
//        if (other == null) return false;
//        LicenseStatus otherStatus = (LicenseStatus) other;
//        return  getLicenseStatusId() == otherStatus.getLicenseStatusId() &&
//                getDescrip().equals(otherStatus.getDescrip());
//    }
    
    
    
    @Override
    public String toString(){
        return getDescrip();
    }

    public boolean compares(LicenseStatus other) {
        if (other == null) return false;
        return  getLicenseStatusId() == other.getLicenseStatusId() &&
                getDescrip().equals(other.getDescrip());
    }
}
