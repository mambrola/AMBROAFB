/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings;

import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public class Logging extends EditorPanelable {

    private final StringProperty licenseNumber;
    private final StringProperty email;
    private final StringProperty logginDateStr;
    private final StringProperty macAddress;
    private final StringProperty response;
    private final ObjectProperty<LocalDate> logginDateObj;
    
    public Logging(){
        licenseNumber = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        logginDateStr = new SimpleStringProperty("");
        macAddress = new SimpleStringProperty("");
        response = new SimpleStringProperty("");
        logginDateObj = new SimpleObjectProperty<>();
    }
    
    // Properties:
    public StringProperty licenseNumberProperty(){
        return licenseNumber;
    }
    
    public StringProperty emailProperty(){
        return email;
    }
    
    public ObjectProperty<LocalDate> logginDateProperty(){
        return logginDateObj;
    }
    
    public StringProperty macAddressProperty(){
        return macAddress;
    }
    
    public StringProperty responseProperty(){
        return response;
    }
    
    
    
    // Getters:
    public String getLicenseNumber(){
        return licenseNumber.get();
    }
    
    public String getEmail(){
        return email.get();
    }
    
    public String getLogginDate(){
        return logginDateStr.get();
    }
    
    public String getMacAddress(){
        return macAddress.get();
    }
    
    public String getResponse(){
        return response.get();
    }
    
    
    // Setters:
    public void setLicenseNumber(String licenseNumber){
        this.licenseNumber.set(licenseNumber);
    }
    
    public void setEmail(String email){
        this.email.set(email);
    }
    
    public void setLogginDate(String logginDate){
        logginDateObj.set(DateConverter.getInstance().parseDate(logginDate));
        
//        String logDate;
//        try {
//            logginDateObj.set(DateConverter.parseDateWithTimeAndMilisecond(logginDate));
//            logDate = DateConverter.getDayMonthnameYearBySpace(logginDateObj.get());
//        } catch (Exception ex){
//            logDate = logginDate;
//        }
//        logginDateStr.set(logDate);
    }
    
    public void setMacAddress(String macAddress){
        this.macAddress.set(macAddress);
    }
    
    public void setResponse(String response){
        this.response.set(response);
    }
        
    
    @Override
    public Logging cloneWithoutID() {
        Logging clone = new Logging();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Logging cloneWithID() {
        Logging clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Logging logging = (Logging)other;
        setLicenseNumber(logging.getLicenseNumber());
        setEmail(logging.getEmail());
        setLogginDate(logging.getLogginDate());
        setMacAddress(logging.getMacAddress());
        setResponse(logging.getResponse());
    }

    @Override
    public String toStringForSearch() {
        return getMacAddress();
    }
    
    
    @Override
    public boolean compares(EditorPanelable backup){
        Logging loggingBackup = (Logging) backup;
        return  getLicenseNumber().equals(loggingBackup.getLicenseNumber()) &&
                getEmail().equals(loggingBackup.getEmail()) &&
                getLogginDate().equals(loggingBackup.getLogginDate()) &&
                getMacAddress().equals(loggingBackup.getMacAddress()) &&
                getResponse().equals(loggingBackup.getResponse());
                
    }
}
