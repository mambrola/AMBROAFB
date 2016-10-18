/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.helper;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dato
 */
public class LicenseStatus {
    
    private final SimpleStringProperty descrip;
    private int recId;
    private int licenseStatusId;
    private String language; // JsonIgnorable ??
    
    public LicenseStatus(){
        descrip = new SimpleStringProperty("");
    }
    
    public int getRecId(){
        return recId;
    }
    
    public int getLicenseStatusId(){
        return licenseStatusId;
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
        this.licenseStatusId = licenseStatusId;
    }
    
    public void setLanguage(String language){
        this.language = language;
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public boolean equals(LicenseStatus other){
        System.out.println("this.getRecId() == other.getRecId(): " + (this.getRecId() == other.getRecId()));
        System.out.println("this.getLicenseStatusId() == other.getLicenseStatusId(): " + (this.getLicenseStatusId() == other.getLicenseStatusId()));
        System.out.println("this.getDescrip().equals(other.getDescrip()): " + (this.getDescrip().equals(other.getDescrip())));
        
        return  this.getRecId() == other.getRecId() &&
                this.getLicenseStatusId() == other.getLicenseStatusId() &&
                this.getDescrip().equals(other.getDescrip());
    }
    
    @Override
    public String toString(){
        return getDescrip();
    }
}
