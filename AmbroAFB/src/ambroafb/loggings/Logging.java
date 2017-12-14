/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.DateCellFactory;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public class Logging extends EditorPanelable {

    @AView.Column(title = "%license_N", width = TableColumnFeatures.Width.LICENSE, styleClass = TableColumnFeatures.Style.TEXT_CENTER)
    private final StringProperty licenseNumber;

    @AView.Column(title = "%client", width = TableColumnFeatures.Width.CLIENT_MAIL)
    private final StringExpression clientDescrip;
    private final ObjectProperty<Client> clientObj;
    
    @AView.Column(title = "%login_time", width = TableColumnFeatures.Width.DATETIME, styleClass = TableColumnFeatures.Style.TEXT_RIGHT, cellFactory = DateCellFactory.LocalDateTimeCell.class)
    private final ObjectProperty<LocalDateTime> loginDateObj;
    
    @AView.Column(title = "%mac_address", width = TableColumnFeatures.Width.MAC_ADDRESS, styleClass = TableColumnFeatures.Style.TEXT_CENTER)
    private final StringProperty macAddress;
    
    @AView.Column(title = "%login_response", width = "150")
    private final StringProperty response;
    
    private final IntegerProperty licenseId;
    private final IntegerProperty invoiceId;
    
    private static final String DB_LOGIN_VIEW = "logins_by_license_whole";
    
    
    public Logging(){
        licenseNumber = new SimpleStringProperty("");
        clientObj = new SimpleObjectProperty<>(new Client());
        clientDescrip = clientObj.get().getShortDescrip(", ");
        loginDateObj = new SimpleObjectProperty<>();
        macAddress = new SimpleStringProperty("");
        response = new SimpleStringProperty("");
        licenseId = new SimpleIntegerProperty(0);
        invoiceId = new SimpleIntegerProperty(0);
    }

    
    // Properties:
    public StringProperty licenseNumberProperty(){
        return licenseNumber;
    }
    
    public StringProperty macAddressProperty(){
        return macAddress;
    }
    
    public ObjectProperty<Client> clientProperty(){
        return clientObj;
    }
    
    public ObjectProperty<LocalDateTime> loginDateProperty(){
        return loginDateObj;
    }
    
    public StringProperty responseCodeProperty(){
        return response;
    }
    
    
    
    // Getters:
    public String getLicenseNumber(){
        return licenseNumber.get();
    }
    
    public int getClientId(){
        return clientObj.get().getRecId();
    }
    
    public String getFirstName(){
        return clientObj.get().getFirstName();
    }
    
    public String getLastName(){
        return clientObj.get().getLastName();
    }
    
    public String getEmail(){
        return clientObj.get().getEmail();
    }
    
    public String getLoginTime(){
        return (loginDateObj.get() == null) ? null : loginDateObj.toString();
    }
    
    public String getMacAddress(){
        return macAddress.get();
    }
    
    public String getResponseCode(){
        return response.get();
    }
    
    public int getLicenseId(){
        return licenseId.get();
    }
    
    public int getInvoiceId(){
        return invoiceId.get();
    }
    
    
    
    // Setters:
    public void setLicenseNumber(String licenseNumber){
        this.licenseNumber.set(licenseNumber);
    }
    
    public void setClientId(int recId){
        this.clientObj.get().setRecId(recId);
    }
    
    public void setFirstName(String name){
        this.clientObj.get().setFirstName(name);
    }
    
    public void setLastName(String lastName){
        this.clientObj.get().setLastName(lastName);
    }
    
    public void setEmail(String email){
        this.clientObj.get().setEmail(email);
    }
    
    public void setMacAddress(String macAddress){
        this.macAddress.set(macAddress);
    }
    
    public void setLoginTime(String logginTime){
        loginDateObj.set(DateConverter.getInstance().parseDateTime(logginTime));
    }
    
    public void setResponseCode(String response){
        this.response.set(response);
    }
    
    public void setLicenseId(int id){
        licenseId.set(id);
    }

    public void setInvoiceId(int id){
        invoiceId.set(id);
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
        setClientId(logging.getClientId());
        setFirstName(logging.getFirstName());
        setLastName(logging.getLastName());
        setEmail(logging.getEmail());
        setLoginTime(logging.getLoginTime());
        setMacAddress(logging.getMacAddress());
        setResponseCode(logging.getResponseCode());
    }

    @Override
    public String toStringForSearch() {
        return getLicenseNumber() + " " + clientObj.get().getShortDescrip("") + " " + getMacAddress();
    }
    
    
    @Override
    public boolean compares(EditorPanelable backup){
        Logging logingBackup = (Logging) backup;
        return  getLicenseNumber().equals(logingBackup.getLicenseNumber()) &&
                clientObj.get().equals(logingBackup.clientProperty().get()) &&
                Objects.equals(getLoginTime(), logingBackup.getLoginTime()) &&
                getMacAddress().equals(logingBackup.getMacAddress()) &&
                getResponseCode().equals(logingBackup.getResponseCode());
    }
}
