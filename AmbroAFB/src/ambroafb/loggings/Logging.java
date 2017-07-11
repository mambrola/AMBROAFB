/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import ambroafb.loggings.filter.LoggingFilterModel;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class Logging extends EditorPanelable {

    @AView.Column(title = "%license_N", width = "100", styleClass = "textCenter")
    private final StringProperty licenseNumber;

    @AView.Column(title = "%clients", width = "150")
    @JsonIgnore
    private final StringExpression clientDescrip;
    @JsonIgnore
    private final ObjectProperty<Client> clientObj;
    
    @AView.Column(title = "%login_time", width = TableColumnWidths.DATE, styleClass = "textCenter")
    @JsonIgnore
    private final StringProperty loginDateDescrip;
    @JsonIgnore
    private final ObjectProperty<LocalDate> loginDateObj;
    
    @AView.Column(title = "%mac_address", width = "120", styleClass = "textCenter")
    private final StringProperty macAddress;
    
    @AView.Column(title = "%login_response", width = "100", styleClass = "textCenter")
    private final StringProperty response;
    
    private final IntegerProperty licenseId;
    private final IntegerProperty invoiceId;
    
    @JsonIgnore
    private static final String DB_LOGIN_VIEW = "logins_by_license_whole";
    
    
    public Logging(){
        licenseNumber = new SimpleStringProperty("");
        clientObj = new SimpleObjectProperty<>(new Client());
        clientDescrip = clientObj.get().getShortDescrip(", ");
        loginDateObj = new SimpleObjectProperty<>();
        loginDateDescrip = new SimpleStringProperty("");
        macAddress = new SimpleStringProperty("");
        response = new SimpleStringProperty("");
        licenseId = new SimpleIntegerProperty(0);
        invoiceId = new SimpleIntegerProperty(0);
    }

    // DB functions:
    public static ArrayList<Logging> getFilteredFromDB(FilterModel model) {
        LoggingFilterModel logingFilterModel = (LoggingFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where()
                                        .and("login_time", ">=", logingFilterModel.getFromDateForDB())
                                        .and("login_time", "<=", logingFilterModel.getToDateForDB());
        if (!logingFilterModel.isSelectedConcreteClient()){
            Client client = logingFilterModel.getSelectedClient();
            whereBuilder.and("first_name", "=", client.getFirstName())
                        .and("last_name", "=", client.getLastName())
                        .and("email", "=", client.getEmail());
        }
        
        JSONObject params = whereBuilder.condition().build();
        ArrayList<Logging> loggings = DBUtils.getObjectsListFromDB(Logging.class, DB_LOGIN_VIEW, params);
        loggings.sort((Logging log1, Logging log2) -> log2.getRecId() - log1.getRecId());
        return loggings;
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
    
    public ObjectProperty<LocalDate> loginDateProperty(){
        return loginDateObj;
    }
    
    public StringProperty responseCodeProperty(){
        return response;
    }
    
    
    
    // Getters:
    public String getLicenseNumber(){
        return licenseNumber.get();
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
        return loginDateDescrip.get();
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
        loginDateObj.set(DateConverter.getInstance().parseDate(logginTime));
        loginDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(loginDateObj.get()));
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
                Utils.dateEquals(loginDateProperty().get(), logingBackup.loginDateProperty().get()) &&
//                getLoginTime().equals(loggingBackup.getLoginTime()) &&
                getMacAddress().equals(logingBackup.getMacAddress()) &&
                getResponseCode().equals(logingBackup.getResponseCode());
    }
}
